package com.tecmarket.backend.review.service;

import com.tecmarket.backend.review.dto.BusinessReviewResponse;
import com.tecmarket.backend.review.dto.CreateBusinessReviewRequest;
import com.tecmarket.backend.review.dto.PublicBusinessDetailResponse;
import com.tecmarket.backend.review.dto.PublicBusinessResponse;
import com.tecmarket.backend.review.dto.PublicBusinessReviewResponse;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.review.entity.BusinessReview;
import com.tecmarket.backend.repairorder.entity.RepairOrder;
import com.tecmarket.backend.review.mapper.BusinessReviewMapper;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.review.repository.BusinessReviewRepository;
import com.tecmarket.backend.repairorder.repository.RepairOrderRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class BusinessReviewService {

    private static final String DELIVERED_STATUS_CODE = "DELIVERED";

    private final BusinessReviewRepository businessReviewRepository;
    private final BusinessRepository businessRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final AppUserRepository appUserRepository;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessReviewMapper businessReviewMapper;

    public BusinessReviewService(
            BusinessReviewRepository businessReviewRepository,
            BusinessRepository businessRepository,
            RepairOrderRepository repairOrderRepository,
            AppUserRepository appUserRepository,
            BusinessAccessGuard businessAccessGuard,
            BusinessReviewMapper businessReviewMapper) {
        this.businessReviewRepository = businessReviewRepository;
        this.businessRepository = businessRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.appUserRepository = appUserRepository;
        this.businessAccessGuard = businessAccessGuard;
        this.businessReviewMapper = businessReviewMapper;
    }

    @Transactional
    public BusinessReviewResponse create(Integer businessId, CreateBusinessReviewRequest request, String subject) {
        Integer userId = parseUserId(subject);
        AppUser customer = findActiveUser(userId);
        Business business = findBusiness(businessId);

        RepairOrder repairOrder = repairOrderRepository
                .findByIdAndBusinessIdAndDeletedAtIsNull(request.repairOrderId(), businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_review.repair_order.not_found"));

        if (!repairOrder.getCustomer().getId().equals(customer.getId())) {
            throw new AccessDeniedException("business_review.repair_order.customer_mismatch");
        }

        String statusCode = repairOrder.getStatus() != null ? repairOrder.getStatus().getCode() : null;
        if (!DELIVERED_STATUS_CODE.equalsIgnoreCase(statusCode)) {
            throw new ConflictException("business_review.repair_order.status.invalid");
        }

        if (businessReviewRepository.existsByRepairOrderIdAndDeletedAtIsNull(repairOrder.getId())) {
            throw new ConflictException("business_review.repair_order.already_reviewed");
        }

        BusinessReview entity = new BusinessReview();
        entity.setBusiness(business);
        entity.setCustomer(customer);
        entity.setRepairOrder(repairOrder);
        entity.setRating(request.rating());
        entity.setComment(normalizeNullable(request.comment()));
        entity.setActive(true);

        BusinessReview saved = businessReviewRepository.save(entity);
        refreshBusinessRating(business);

        return businessReviewMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<BusinessReviewResponse> findByBusiness(
            Integer businessId, Integer page, Integer size, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "REVIEW_VIEW");
        findBusiness(businessId);

        return PageResponse.from(businessReviewRepository
                .findByBusinessIdAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
                        businessId, PageRequestFactory.of(page, size))
                .map(businessReviewMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<PublicBusinessReviewResponse> findPublicByBusiness(
            Integer businessId, Integer page, Integer size) {
        findActiveBusiness(businessId);

        return PageResponse.from(businessReviewRepository
                .findByBusinessIdAndActiveTrueAndDeletedAtIsNullOrderByCreatedAtDescIdDesc(
                        businessId, PageRequestFactory.of(page, size))
                .map(businessReviewMapper::toPublicResponse));
    }

    @Transactional(readOnly = true)
    public PublicBusinessDetailResponse findPublicBusinessById(Integer businessId) {
        Business business = findActiveBusiness(businessId);
        return businessReviewMapper.toPublicBusinessDetailResponse(business);
    }

    @Transactional(readOnly = true)
    public PageResponse<PublicBusinessResponse> findPublicBusinesses(
            String query,
            Integer cityId,
            BigDecimal minRating,
            Integer page,
            Integer size,
            String sortBy,
            String sortDir) {
        Pageable pageable = buildPageable(page, size, sortBy, sortDir);
        String normalizedQuery = normalizeQuery(query);

        Page<Business> result = StringUtils.hasText(normalizedQuery)
                ? businessRepository.findPublicBusinessesByQuery(normalizedQuery, cityId, minRating, pageable)
                : businessRepository.findPublicBusinesses(cityId, minRating, pageable);

        return PageResponse.from(result.map(businessReviewMapper::toPublicBusinessResponse));
    }

    private void refreshBusinessRating(Business business) {
        Object[] resume = businessReviewRepository.summarizeRatingByBusiness(business.getId());

        double average = 0.0d;
        long count = 0L;
        if (resume != null && resume.length == 2) {
            Object avgValue = resume[0];
            Object countValue = resume[1];

            if (avgValue instanceof Number number) {
                average = number.doubleValue();
            }
            if (countValue instanceof Number number) {
                count = number.longValue();
            }
        }

        business.setRatingAverage(BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP));
        business.setRatingCount(Math.toIntExact(count));
        businessRepository.save(business);
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private Business findActiveBusiness(Integer businessId) {
        return businessRepository.findByIdAndActiveTrueAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private AppUser findActiveUser(Integer userId) {
        return appUserRepository.findById(userId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()) && value.getDeletedAt() == null)
                .orElseThrow(() -> new ResourceNotFoundException("user.not_found"));
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private String normalizeQuery(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private Pageable buildPageable(Integer page, Integer size, String sortBy, String sortDir) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;

        String safeSort = switch (sortBy == null ? "" : sortBy.trim()) {
            case "name" -> "name";
            case "ratingCount" -> "ratingCount";
            case "createdAt" -> "createdAt";
            case "ratingAverage" -> "ratingAverage";
            default -> "ratingAverage";
        };

        return PageRequestFactory.of(page, size, Sort.by(direction, safeSort));
    }

    private Integer parseUserId(String subject) {
        try {
            return Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("auth.token.invalid");
        }
    }
}
