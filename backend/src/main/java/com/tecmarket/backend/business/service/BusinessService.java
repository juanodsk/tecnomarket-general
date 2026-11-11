package com.tecmarket.backend.business.service;

import com.tecmarket.backend.business.dto.BusinessResponse;
import com.tecmarket.backend.business.dto.CreateBusinessCatalogRequest;
import com.tecmarket.backend.business.dto.UpdateBusinessCatalogRequest;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.mapper.BusinessMapper;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.repository.BusinessUserRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.FileValidationException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.parameter.repository.CityRepository;
import com.tecmarket.backend.user.entity.DocumentType;
import com.tecmarket.backend.user.repository.DocumentTypeRepository;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import com.tecmarket.backend.storage.config.MinioStorageProperties;
import com.tecmarket.backend.storage.dto.StoredObjectResponse;
import com.tecmarket.backend.storage.service.MinioStorageService;
import com.tecmarket.backend.storage.service.StorageDeletionQueueService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class BusinessService {
    private static final String PLATFORM_ADMIN_ROLE_CODE = "PLATFORM_ADMIN";

    private final BusinessRepository businessRepository;
    private final BusinessUserRepository businessUserRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final CityRepository cityRepository;
    private final UserRoleRepository userRoleRepository;
    private final BusinessAccessGuard businessAccessGuard;
    private final BusinessMapper businessMapper;
    private final BusinessBranchService businessBranchService;
    private final BusinessCatalogInitializerService businessCatalogInitializerService;
    private final MinioStorageService minioStorageService;
    private final MinioStorageProperties storageProperties;
    private final StorageDeletionQueueService storageDeletionQueueService;

    public BusinessService(
            BusinessRepository businessRepository,
            BusinessUserRepository businessUserRepository,
            DocumentTypeRepository documentTypeRepository,
            CityRepository cityRepository,
            UserRoleRepository userRoleRepository,
            BusinessAccessGuard businessAccessGuard,
            BusinessMapper businessMapper,
            BusinessBranchService businessBranchService,
            BusinessCatalogInitializerService businessCatalogInitializerService,
            MinioStorageService minioStorageService,
            MinioStorageProperties storageProperties,
            StorageDeletionQueueService storageDeletionQueueService) {
        this.businessRepository = businessRepository;
        this.businessUserRepository = businessUserRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.cityRepository = cityRepository;
        this.userRoleRepository = userRoleRepository;
        this.businessAccessGuard = businessAccessGuard;
        this.businessMapper = businessMapper;
        this.businessBranchService = businessBranchService;
        this.businessCatalogInitializerService = businessCatalogInitializerService;
        this.minioStorageService = minioStorageService;
        this.storageProperties = storageProperties;
        this.storageDeletionQueueService = storageDeletionQueueService;
    }

    @Transactional(readOnly = true)
    public PageResponse<BusinessResponse> findAll(Integer page, Integer size, String subject) {
        Integer userId = parseUserId(subject);
        if (isPlatformAdmin(userId)) {
            return PageResponse.from(businessRepository
                    .findAllByDeletedAtIsNullOrderByNameAsc(PageRequestFactory.of(page, size))
                    .map(businessMapper::toResponse));
        }

        return PageResponse.from(businessUserRepository
                .findActiveBusinessesByUserId(userId, PageRequestFactory.of(page, size))
                .map(businessMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public List<BusinessResponse> findMyBusinesses(String subject) {
        Integer userId = parseUserId(subject);

        return businessUserRepository.findActiveBusinessesByUserId(userId)
                .stream()
                .map(businessMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BusinessResponse findById(Integer id, String subject) {
        businessAccessGuard.assertHasBusinessPermission(id, subject, "BUSINESS_VIEW");

        Business business = businessRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));

        return businessMapper.toResponse(business);
    }

    @Transactional
    public BusinessResponse create(CreateBusinessCatalogRequest request, String subject) {
        businessAccessGuard.assertPlatformAdmin(subject);
        String documentTypeCode = request.documentTypeCode().trim().toUpperCase();

        DocumentType documentType = documentTypeRepository.findByCodeAndActiveTrue(documentTypeCode)
                .orElseThrow(() -> new ResourceNotFoundException("document_code.not_found"));

        City city = cityRepository.findById(request.cityId())
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("city.not_found"));

        String documentNumber = request.documentNumber().trim();
        if (businessRepository.existsByDocumentTypeAndDocumentNumberAndDeletedAtIsNull(documentType, documentNumber)) {
            throw new ConflictException("business.document.exists");
        }

        Business business = new Business();
        business.setDocumentType(documentType);
        business.setDocumentNumber(documentNumber);
        business.setName(request.name().trim());
        business.setLegalName(request.legalName().trim());
        business.setEmail(request.email().trim().toLowerCase());
        business.setPhone(request.phone().trim());
        business.setAddress(request.address().trim());
        business.setDescription(request.description().trim());
        business.setCity(city);
        assertAssetUploadedSeparately(request.logo());
        assertAssetUploadedSeparately(request.coverImage());
        business.setLogo(null);
        business.setCoverImage(null);
        business.setRatingAverage(BigDecimal.ZERO);
        business.setRatingCount(0);
        business.setActive(true);

        Business saved = businessRepository.save(business);
        businessBranchService.createPrimaryBranchFromBusiness(saved);
        businessCatalogInitializerService.initialize(saved);
        return businessMapper.toResponse(saved);
    }

    @Transactional
    public BusinessResponse update(Integer id, UpdateBusinessCatalogRequest request, String subject) {
        businessAccessGuard.assertHasBusinessPermission(id, subject, "BUSINESS_UPDATE");

        Business business = businessRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));

        City city = cityRepository.findById(request.cityId())
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("city.not_found"));

        business.setName(request.name().trim());
        business.setLegalName(request.legalName().trim());
        business.setEmail(request.email().trim().toLowerCase());
        business.setPhone(request.phone().trim());
        business.setAddress(request.address().trim());
        business.setDescription(request.description().trim());
        business.setCity(city);
        replaceLogoReference(business, requestedAssetReference(business.getLogo(), request.logo()));
        replaceCoverReference(business, requestedAssetReference(business.getCoverImage(), request.coverImage()));

        if (request.active() != null) {
            business.setActive(request.active());
            if (Boolean.FALSE.equals(request.active())) {
                business.setDeletedAt(LocalDateTime.now());
            } else {
                business.setDeletedAt(null);
            }
        }

        return businessMapper.toResponse(business);
    }

    @Transactional
    public void delete(Integer id, String subject) {
        businessAccessGuard.assertHasBusinessPermission(id, subject, "BUSINESS_UPDATE");

        Business business = businessRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));

        business.setActive(false);
        business.setDeletedAt(LocalDateTime.now());
        enqueueManagedAsset(business.getLogoBucketName(), business.getLogoObjectKey(), "BUSINESS_DELETED", "BUSINESS", id);
        enqueueManagedAsset(business.getCoverImageBucketName(), business.getCoverImageObjectKey(), "BUSINESS_DELETED", "BUSINESS", id);
    }

    @Transactional
    public BusinessResponse uploadLogo(Integer id, MultipartFile file, String subject) {
        Business business = findAccessibleBusiness(id, subject);
        StoredObjectResponse uploaded = minioStorageService.uploadPublicImage(
                storageProperties.getBusinessAssetsBucket(),
                "businesses/" + id + "/logo",
                file);
        minioStorageService.registerRollbackCleanup(uploaded);
        enqueueManagedAsset(business.getLogoBucketName(), business.getLogoObjectKey(), "ASSET_REPLACED", "BUSINESS_LOGO", id);
        business.setLogo(uploaded.url());
        business.setLogoBucketName(uploaded.bucket());
        business.setLogoObjectKey(uploaded.objectKey());
        return businessMapper.toResponse(business);
    }

    @Transactional
    public BusinessResponse uploadCover(Integer id, MultipartFile file, String subject) {
        Business business = findAccessibleBusiness(id, subject);
        StoredObjectResponse uploaded = minioStorageService.uploadPublicImage(
                storageProperties.getBusinessAssetsBucket(),
                "businesses/" + id + "/cover",
                file);
        minioStorageService.registerRollbackCleanup(uploaded);
        enqueueManagedAsset(business.getCoverImageBucketName(), business.getCoverImageObjectKey(), "ASSET_REPLACED", "BUSINESS_COVER", id);
        business.setCoverImage(uploaded.url());
        business.setCoverImageBucketName(uploaded.bucket());
        business.setCoverImageObjectKey(uploaded.objectKey());
        return businessMapper.toResponse(business);
    }

    private Business findAccessibleBusiness(Integer id, String subject) {
        businessAccessGuard.assertHasBusinessPermission(id, subject, "BUSINESS_UPDATE");
        return businessRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private void assertAssetUploadedSeparately(String requestedUrl) {
        if (StringUtils.hasText(requestedUrl)) {
            throw new FileValidationException("storage.public_asset.upload_required");
        }
    }

    private String requestedAssetReference(String currentUrl, String requestedUrl) {
        String normalized = normalizeNullable(requestedUrl);
        if (normalized == null || Objects.equals(currentUrl, normalized)) {
            return normalized;
        }
        throw new FileValidationException("storage.public_asset.upload_required");
    }

    private void replaceLogoReference(Business business, String newUrl) {
        if (Objects.equals(business.getLogo(), newUrl)) {
            return;
        }
        enqueueManagedAsset(business.getLogoBucketName(), business.getLogoObjectKey(), "ASSET_REFERENCE_CHANGED", "BUSINESS_LOGO", business.getId());
        business.setLogo(newUrl);
        business.setLogoBucketName(null);
        business.setLogoObjectKey(null);
    }

    private void replaceCoverReference(Business business, String newUrl) {
        if (Objects.equals(business.getCoverImage(), newUrl)) {
            return;
        }
        enqueueManagedAsset(business.getCoverImageBucketName(), business.getCoverImageObjectKey(), "ASSET_REFERENCE_CHANGED", "BUSINESS_COVER", business.getId());
        business.setCoverImage(newUrl);
        business.setCoverImageBucketName(null);
        business.setCoverImageObjectKey(null);
    }

    private void enqueueManagedAsset(
            String bucket,
            String objectKey,
            String reason,
            String sourceType,
            Integer sourceId) {
        storageDeletionQueueService.enqueue(bucket, objectKey, reason, sourceType, sourceId, LocalDateTime.now());
    }

    private Integer parseUserId(String subject) {
        try {
            return Integer.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new BadCredentialsException("auth.token.invalid");
        }
    }

    private boolean isPlatformAdmin(Integer userId) {
        return userRoleRepository.findActiveRoleCodesByUserId(userId)
                .stream()
                .anyMatch(PLATFORM_ADMIN_ROLE_CODE::equals);
    }
}
