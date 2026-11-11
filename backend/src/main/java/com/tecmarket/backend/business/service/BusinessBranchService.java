package com.tecmarket.backend.business.service;

import com.tecmarket.backend.business.dto.BusinessBranchResponse;
import com.tecmarket.backend.business.dto.CreateBusinessBranchRequest;
import com.tecmarket.backend.business.dto.UpdateBusinessBranchRequest;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.mapper.BusinessBranchMapper;
import com.tecmarket.backend.business.repository.BusinessBranchRepository;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.parameter.repository.CityRepository;
import com.tecmarket.backend.subscription.repository.BusinessSubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BusinessBranchService {

    private static final int DEFAULT_BRANCH_LIMIT_WITHOUT_SUBSCRIPTION = 1;

    private final BusinessRepository businessRepository;
    private final BusinessBranchRepository businessBranchRepository;
    private final CityRepository cityRepository;
    private final BusinessSubscriptionRepository businessSubscriptionRepository;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessBranchMapper businessBranchMapper;

    public BusinessBranchService(
            BusinessRepository businessRepository,
            BusinessBranchRepository businessBranchRepository,
            CityRepository cityRepository,
            BusinessSubscriptionRepository businessSubscriptionRepository,
            BusinessAccessGuard businessAccessGuard,
            BusinessBranchMapper businessBranchMapper) {
        this.businessRepository = businessRepository;
        this.businessBranchRepository = businessBranchRepository;
        this.cityRepository = cityRepository;
        this.businessSubscriptionRepository = businessSubscriptionRepository;
        this.businessAccessGuard = businessAccessGuard;
        this.businessBranchMapper = businessBranchMapper;
    }

    @Transactional(readOnly = true)
    public List<BusinessBranchResponse> findAll(Integer businessId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_BRANCH_VIEW");
        findBusiness(businessId);

        return businessBranchRepository.findByBusinessIdAndDeletedAtIsNullOrderByPrimaryBranchDescNameAscIdAsc(businessId)
                .stream()
                .map(businessBranchMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BusinessBranchResponse findById(Integer businessId, Integer branchId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_BRANCH_VIEW");
        return businessBranchMapper.toResponse(findBranch(businessId, branchId));
    }

    @Transactional
    public BusinessBranchResponse create(Integer businessId, CreateBusinessBranchRequest request, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_BRANCH_CREATE");
        Business business = findBusiness(businessId);
        validateBranchLimit(businessId);

        String name = request.name().trim();
        if (businessBranchRepository.existsByBusinessIdAndNameIgnoreCaseAndDeletedAtIsNull(businessId, name)) {
            throw new ConflictException("business_branch.name.exists");
        }

        BusinessBranch branch = new BusinessBranch();
        branch.setBusiness(business);
        branch.setBranchNumber(nextBranchNumber(businessId));
        apply(branch, request.cityId(), name, request.address(), request.neighborhood(), request.phone(),
                request.latitude(), request.longitude(), request.serviceRadiusKm(), request.primaryBranch(), true);

        boolean firstBranch = businessBranchRepository.countByBusinessIdAndActiveTrueAndDeletedAtIsNull(businessId) == 0;
        if (firstBranch) {
            branch.setPrimaryBranch(true);
        }
        if (Boolean.TRUE.equals(branch.getPrimaryBranch())) {
            clearPrimaryBranch(businessId, null);
        }

        return businessBranchMapper.toResponse(businessBranchRepository.save(branch));
    }

    @Transactional
    public BusinessBranchResponse update(Integer businessId, Integer branchId, UpdateBusinessBranchRequest request, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_BRANCH_UPDATE");
        BusinessBranch branch = findBranch(businessId, branchId);

        String name = request.name().trim();
        if (businessBranchRepository.existsByBusinessIdAndNameIgnoreCaseAndDeletedAtIsNullAndIdNot(businessId, name, branchId)) {
            throw new ConflictException("business_branch.name.exists");
        }

        boolean disabling = Boolean.FALSE.equals(request.active()) && Boolean.TRUE.equals(branch.getActive());
        if (disabling && businessBranchRepository.countByBusinessIdAndActiveTrueAndDeletedAtIsNull(businessId) <= 1) {
            throw new ConflictException("business_branch.minimum_required");
        }

        apply(branch, request.cityId(), name, request.address(), request.neighborhood(), request.phone(),
                request.latitude(), request.longitude(), request.serviceRadiusKm(), request.primaryBranch(), request.active());

        if (Boolean.TRUE.equals(branch.getPrimaryBranch())) {
            clearPrimaryBranch(businessId, branchId);
        }
        if (Boolean.FALSE.equals(branch.getActive())) {
            branch.setPrimaryBranch(false);
            branch.setDeletedAt(LocalDateTime.now());
        } else {
            branch.setDeletedAt(null);
        }

        return businessBranchMapper.toResponse(branch);
    }

    @Transactional
    public void delete(Integer businessId, Integer branchId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_BRANCH_UPDATE");
        BusinessBranch branch = findBranch(businessId, branchId);

        if (businessBranchRepository.countByBusinessIdAndActiveTrueAndDeletedAtIsNull(businessId) <= 1) {
            throw new ConflictException("business_branch.minimum_required");
        }

        branch.setActive(false);
        branch.setPrimaryBranch(false);
        branch.setDeletedAt(LocalDateTime.now());
    }

    @Transactional
    public BusinessBranch createPrimaryBranchFromBusiness(Business business) {
        if (businessBranchRepository.countByBusinessIdAndActiveTrueAndDeletedAtIsNull(business.getId()) > 0) {
            return businessBranchRepository.findByBusinessIdAndPrimaryBranchTrueAndDeletedAtIsNull(business.getId())
                    .orElse(null);
        }

        BusinessBranch branch = new BusinessBranch();
        branch.setBusiness(business);
        branch.setBranchNumber(1);
        branch.setName("Sede principal");
        branch.setCity(business.getCity());
        branch.setAddress(business.getAddress());
        branch.setPhone(business.getPhone());
        branch.setPrimaryBranch(true);
        branch.setActive(true);
        return businessBranchRepository.save(branch);
    }

    private Integer nextBranchNumber(Integer businessId) {
        return businessBranchRepository.findFirstByBusinessIdAndDeletedAtIsNullOrderByBranchNumberDesc(businessId)
                .map(branch -> branch.getBranchNumber() + 1)
                .orElse(1);
    }

    private void apply(
            BusinessBranch branch,
            Integer cityId,
            String name,
            String address,
            String neighborhood,
            String phone,
            BigDecimal latitude,
            BigDecimal longitude,
            BigDecimal serviceRadiusKm,
            Boolean primaryBranch,
            Boolean active) {
        validateCoordinates(latitude, longitude);
        City city = cityRepository.findById(cityId)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("city.not_found"));

        branch.setName(name);
        branch.setCity(city);
        branch.setAddress(address.trim());
        branch.setNeighborhood(normalizeNullable(neighborhood));
        branch.setPhone(normalizeNullable(phone));
        branch.setLatitude(scaleNullable(latitude, 7));
        branch.setLongitude(scaleNullable(longitude, 7));
        branch.setServiceRadiusKm(scaleNullable(serviceRadiusKm, 2));
        branch.setPrimaryBranch(primaryBranch != null && primaryBranch);
        branch.setActive(active == null || active);
    }

    private void validateCoordinates(BigDecimal latitude, BigDecimal longitude) {
        if ((latitude == null && longitude != null) || (latitude != null && longitude == null)) {
            throw new ConflictException("business_branch.coordinates.incomplete");
        }
    }

    private void validateBranchLimit(Integer businessId) {
        long activeBranches = businessBranchRepository.countByBusinessIdAndActiveTrueAndDeletedAtIsNull(businessId);
        int limit = businessSubscriptionRepository.findByBusinessIdAndActiveTrueAndDeletedAtIsNull(businessId)
                .map(subscription -> subscription.getPlan().getMaxBranches())
                .orElse(DEFAULT_BRANCH_LIMIT_WITHOUT_SUBSCRIPTION);

        if (activeBranches >= limit) {
            throw new ConflictException("business_branch.limit.exceeded");
        }
    }

    private void clearPrimaryBranch(Integer businessId, Integer ignoredBranchId) {
        businessBranchRepository.findByBusinessIdAndPrimaryBranchTrueAndDeletedAtIsNull(businessId)
                .filter(branch -> ignoredBranchId == null || !branch.getId().equals(ignoredBranchId))
                .ifPresent(branch -> branch.setPrimaryBranch(false));
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private BusinessBranch findBranch(Integer businessId, Integer branchId) {
        return businessBranchRepository.findByIdAndBusinessIdAndDeletedAtIsNull(branchId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_branch.not_found"));
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private BigDecimal scaleNullable(BigDecimal value, int scale) {
        if (value == null) {
            return null;
        }
        return value.setScale(scale, RoundingMode.HALF_UP);
    }
}
