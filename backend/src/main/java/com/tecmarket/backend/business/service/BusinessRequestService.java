package com.tecmarket.backend.business.service;

import com.tecmarket.backend.business.dto.BusinessRequestResponse;
import com.tecmarket.backend.business.dto.CreateBusinessRequest;
import com.tecmarket.backend.business.dto.RejectBusinessRequest;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessRequest;
import com.tecmarket.backend.business.entity.BusinessRequestStatus;
import com.tecmarket.backend.business.entity.BusinessUser;
import com.tecmarket.backend.business.entity.BusinessBranch;
import com.tecmarket.backend.business.entity.BusinessUserBranch;
import com.tecmarket.backend.business.mapper.BusinessRequestMapper;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessRequestRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.business.repository.BusinessUserBranchRepository;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.parameter.repository.CityRepository;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.repository.RoleRepository;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.entity.DocumentType;
import com.tecmarket.backend.user.repository.AppUserRepository;
import com.tecmarket.backend.user.repository.DocumentTypeRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.auth.service.SecurityVersionService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BusinessRequestService {

    private static final String BUSINESS_OWNER_ROLE_CODE = "BUSINESS_OWNER";

    private final BusinessRequestRepository businessRequestRepository;
    private final BusinessRepository businessRepository;
    private final BusinessUserRepository businessUserRepository;
    private final RoleRepository roleRepository;
    private final AppUserRepository appUserRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final CityRepository cityRepository;
    private final BusinessRequestMapper businessRequestMapper;
    private final BusinessBranchService businessBranchService;
    private final BusinessUserBranchRepository businessUserBranchRepository;
    private final BusinessCatalogInitializerService businessCatalogInitializerService;
    private final BusinessAccessGuard businessAccessGuard;
    private final SecurityVersionService securityVersionService;

    public BusinessRequestService(
            BusinessRequestRepository businessRequestRepository,
            BusinessRepository businessRepository,
            BusinessUserRepository businessUserRepository,
            RoleRepository roleRepository,
            AppUserRepository appUserRepository,
            DocumentTypeRepository documentTypeRepository,
            CityRepository cityRepository,
            BusinessRequestMapper businessRequestMapper,
            BusinessBranchService businessBranchService,
            BusinessUserBranchRepository businessUserBranchRepository,
            BusinessCatalogInitializerService businessCatalogInitializerService,
            BusinessAccessGuard businessAccessGuard,
            SecurityVersionService securityVersionService) {
        this.businessRequestRepository = businessRequestRepository;
        this.businessRepository = businessRepository;
        this.businessUserRepository = businessUserRepository;
        this.roleRepository = roleRepository;
        this.appUserRepository = appUserRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.cityRepository = cityRepository;
        this.businessRequestMapper = businessRequestMapper;
        this.businessBranchService = businessBranchService;
        this.businessUserBranchRepository = businessUserBranchRepository;
        this.businessCatalogInitializerService = businessCatalogInitializerService;
        this.businessAccessGuard = businessAccessGuard;
        this.securityVersionService = securityVersionService;
    }

    @Transactional
    public BusinessRequestResponse create(CreateBusinessRequest request, String subject) {
        AppUser user = findActiveUserBySubject(subject);

        String documentTypeCode = request.documentTypeCode().trim().toUpperCase();
        DocumentType documentType = documentTypeRepository.findByCodeAndActiveTrue(documentTypeCode)
                .orElseThrow(() -> new ResourceNotFoundException("document_code.not_found"));

        City city = cityRepository.findById(request.cityId())
                .filter(cityEntity -> Boolean.TRUE.equals(cityEntity.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("city.not_found"));

        BusinessRequest businessRequest = new BusinessRequest();
        businessRequest.setRequestedByUser(user);
        businessRequest.setDocumentType(documentType);
        businessRequest.setDocumentNumber(request.documentNumber().trim());
        businessRequest.setBusinessName(request.businessName().trim());
        businessRequest.setLegalName(normalizeNullable(request.legalName()));
        businessRequest.setEmail(request.email().trim().toLowerCase());
        businessRequest.setPhone(request.phone().trim());
        businessRequest.setCity(city);
        businessRequest.setAddress(normalizeNullable(request.address()));
        businessRequest.setLegalFile(request.legalFile().trim());
        businessRequest.setStatus(BusinessRequestStatus.PENDING);
        businessRequest.setActive(true);

        BusinessRequest saved = businessRequestRepository.save(businessRequest);
        return businessRequestMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<BusinessRequestResponse> findMyRequests(Integer page, Integer size, String subject) {
        AppUser user = findActiveUserBySubject(subject);

        return PageResponse.from(businessRequestRepository
                .findByRequestedByUserIdAndActiveTrueOrderByCreatedAtDesc(
                        user.getId(), PageRequestFactory.of(page, size))
                .map(businessRequestMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<BusinessRequestResponse> findPendingRequests(Integer page, Integer size, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        return PageResponse.from(businessRequestRepository
                .findByStatusAndActiveTrueOrderByCreatedAtAsc(
                        BusinessRequestStatus.PENDING, PageRequestFactory.of(page, size))
                .map(businessRequestMapper::toResponse));
    }

    @Transactional
    public BusinessRequestResponse approve(Integer requestId, String reviewerSubject) {
        businessAccessGuard.assertPlatformAdmin(reviewerSubject);
        AppUser reviewer = findActiveUserBySubject(reviewerSubject);

        BusinessRequest request = businessRequestRepository.findByIdAndActiveTrue(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("business_request.not_found"));

        if (request.getStatus() != BusinessRequestStatus.PENDING) {
            throw new ConflictException("business_request.status.invalid");
        }

        if (businessRepository.existsByDocumentTypeAndDocumentNumberAndDeletedAtIsNull(
                request.getDocumentType(), request.getDocumentNumber())) {
            throw new ConflictException("business.exists.document");
        }

        Role ownerRole = roleRepository.findByCodeAndActiveTrue(BUSINESS_OWNER_ROLE_CODE)
                .orElseThrow(() -> new ResourceNotFoundException("role.not_found"));

        Business business = new Business();
        business.setDocumentType(request.getDocumentType());
        business.setDocumentNumber(request.getDocumentNumber());
        business.setName(request.getBusinessName());
        business.setLegalName(
                StringUtils.hasText(request.getLegalName()) ? request.getLegalName() : request.getBusinessName());
        business.setEmail(request.getEmail());
        business.setPhone(request.getPhone());
        business.setAddress(
                StringUtils.hasText(request.getAddress()) ? request.getAddress() : "Sin dirección registrada");
        business.setDescription("Negocio creado desde solicitud #" + request.getId());
        business.setCity(request.getCity());
        business.setActive(true);

        Business savedBusiness = businessRepository.save(business);
        BusinessBranch primaryBranch = businessBranchService.createPrimaryBranchFromBusiness(savedBusiness);
        businessCatalogInitializerService.initialize(savedBusiness);

        BusinessUser businessUser = new BusinessUser();
        businessUser.setBusiness(savedBusiness);
        businessUser.setUser(request.getRequestedByUser());
        businessUser.setRole(ownerRole);
        businessUser.setActive(true);
        BusinessUser savedBusinessUser = businessUserRepository.save(businessUser);
        BusinessUserBranch ownerBranch = new BusinessUserBranch();
        ownerBranch.setBusinessUser(savedBusinessUser);
        ownerBranch.setBranch(primaryBranch);
        ownerBranch.setActive(true);
        businessUserBranchRepository.save(ownerBranch);
        securityVersionService.invalidateUser(request.getRequestedByUser());

        request.setStatus(BusinessRequestStatus.APPROVED);
        request.setReviewedByUser(reviewer);
        request.setReviewedAt(LocalDateTime.now());
        request.setRejectionReason(null);

        return businessRequestMapper.toResponse(request);
    }

    @Transactional
    public BusinessRequestResponse reject(Integer requestId, RejectBusinessRequest rejectRequest,
            String reviewerSubject) {
        businessAccessGuard.assertPlatformAdmin(reviewerSubject);
        AppUser reviewer = findActiveUserBySubject(reviewerSubject);

        BusinessRequest request = businessRequestRepository.findByIdAndActiveTrue(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("business_request.not_found"));

        if (request.getStatus() != BusinessRequestStatus.PENDING) {
            throw new ConflictException("business_request.status.invalid");
        }

        request.setStatus(BusinessRequestStatus.REJECTED);
        request.setReviewedByUser(reviewer);
        request.setReviewedAt(LocalDateTime.now());
        request.setRejectionReason(rejectRequest.rejectionReason().trim());

        return businessRequestMapper.toResponse(request);
    }

    private AppUser findActiveUserBySubject(String subject) {
        Integer userId;
        try {
            userId = Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("auth.token.invalid");
        }

        return appUserRepository.findById(userId)
                .filter(appUser -> Boolean.TRUE.equals(appUser.getActive()))
                .orElseThrow(() -> new BadCredentialsException("auth.token.invalid"));
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
