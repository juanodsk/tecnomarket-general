package com.tecmarket.backend.business.service;

import com.tecmarket.backend.business.dto.BusinessOfferingResponse;
import com.tecmarket.backend.business.dto.CreateBusinessOfferingRequest;
import com.tecmarket.backend.business.dto.UpdateBusinessOfferingRequest;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.business.entity.BusinessOffering;
import com.tecmarket.backend.business.mapper.BusinessOfferingMapper;
import com.tecmarket.backend.business.repository.BusinessOfferingRepository;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.storage.config.MinioStorageProperties;
import com.tecmarket.backend.storage.dto.StoredObjectResponse;
import com.tecmarket.backend.storage.service.MinioStorageService;
import com.tecmarket.backend.storage.service.StorageDeletionQueueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
@Service
public class BusinessOfferingService {

    private final BusinessOfferingRepository businessOfferingRepository;
    private final BusinessRepository businessRepository;
    private final BusinessCatalogLookupService businessCatalogLookupService;
    private final BusinessOfferingMapper businessOfferingMapper;
    private final BusinessAccessGuard businessAccessGuard;
    private final MinioStorageService minioStorageService;
    private final MinioStorageProperties storageProperties;
    private final StorageDeletionQueueService storageDeletionQueueService;

    public BusinessOfferingService(
            BusinessOfferingRepository businessOfferingRepository,
            BusinessRepository businessRepository,
            BusinessCatalogLookupService businessCatalogLookupService,
            BusinessOfferingMapper businessOfferingMapper,
            BusinessAccessGuard businessAccessGuard,
            MinioStorageService minioStorageService,
            MinioStorageProperties storageProperties,
            StorageDeletionQueueService storageDeletionQueueService) {
        this.businessOfferingRepository = businessOfferingRepository;
        this.businessRepository = businessRepository;
        this.businessCatalogLookupService = businessCatalogLookupService;
        this.businessOfferingMapper = businessOfferingMapper;
        this.businessAccessGuard = businessAccessGuard;
        this.minioStorageService = minioStorageService;
        this.storageProperties = storageProperties;
        this.storageDeletionQueueService = storageDeletionQueueService;
    }

    @Transactional(readOnly = true)
    public PageResponse<BusinessOfferingResponse> findAllByBusiness(
            Integer businessId, Integer page, Integer size, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_SERVICE_VIEW");
        findBusiness(businessId);
        return PageResponse.from(businessOfferingRepository
                .findByBusinessIdAndDeletedAtIsNullOrderByNameAsc(
                        businessId, PageRequestFactory.of(page, size))
                .map(businessOfferingMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public BusinessOfferingResponse findById(Integer businessId, Integer offeringId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_SERVICE_VIEW");
        BusinessOffering offering = businessOfferingRepository.findByIdAndBusinessIdAndDeletedAtIsNull(offeringId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_service.not_found"));
        return businessOfferingMapper.toResponse(offering);
    }

    @Transactional
    public BusinessOfferingResponse create(Integer businessId, CreateBusinessOfferingRequest request, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_SERVICE_CREATE");
        Business business = findBusiness(businessId);
        BusinessCatalogItem serviceCategory = findServiceCategory(businessId, request.serviceCategoryId());
        BusinessCatalogItem deviceType = findDeviceType(businessId, request.deviceTypeId());

        String normalizedName = request.name().trim();
        if (businessOfferingRepository.existsByBusinessIdAndServiceCategoryIdAndDeviceTypeIdAndNameAndDeletedAtIsNull(
                businessId, serviceCategory.getId(), deviceType.getId(), normalizedName)) {
            throw new ConflictException("business_service.exists");
        }

        BusinessOffering offering = new BusinessOffering();
        offering.setBusiness(business);
        offering.setServiceCategory(serviceCategory);
        offering.setDeviceType(deviceType);
        offering.setName(normalizedName);
        offering.setDescription(request.description().trim());
        offering.setBasePrice(request.basePrice());
        offering.setEstimatedTimeMinutes(request.estimatedTimeMinutes());
        offering.setActive(true);

        return businessOfferingMapper.toResponse(businessOfferingRepository.save(offering));
    }

    @Transactional
    public BusinessOfferingResponse update(
            Integer businessId,
            Integer offeringId,
            UpdateBusinessOfferingRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_SERVICE_UPDATE");
        BusinessOffering offering = businessOfferingRepository.findByIdAndBusinessIdAndDeletedAtIsNull(offeringId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_service.not_found"));

        BusinessCatalogItem serviceCategory = findServiceCategory(businessId, request.serviceCategoryId());
        BusinessCatalogItem deviceType = findDeviceType(businessId, request.deviceTypeId());

        String normalizedName = request.name().trim();
        boolean changedKeyFields = !normalizedName.equals(offering.getName())
                || !serviceCategory.getId().equals(offering.getServiceCategory().getId())
                || !deviceType.getId().equals(offering.getDeviceType().getId());

        if (changedKeyFields && businessOfferingRepository.existsByBusinessIdAndServiceCategoryIdAndDeviceTypeIdAndNameAndDeletedAtIsNull(
                businessId, serviceCategory.getId(), deviceType.getId(), normalizedName)) {
            throw new ConflictException("business_service.exists");
        }

        offering.setServiceCategory(serviceCategory);
        offering.setDeviceType(deviceType);
        offering.setName(normalizedName);
        offering.setDescription(request.description().trim());
        offering.setBasePrice(request.basePrice());
        offering.setEstimatedTimeMinutes(request.estimatedTimeMinutes());

        if (request.active() != null) {
            offering.setActive(request.active());
            offering.setDeletedAt(Boolean.FALSE.equals(request.active()) ? LocalDateTime.now() : null);
        }

        return businessOfferingMapper.toResponse(offering);
    }

    @Transactional
    public void delete(Integer businessId, Integer offeringId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_SERVICE_UPDATE");
        BusinessOffering offering = businessOfferingRepository.findByIdAndBusinessIdAndDeletedAtIsNull(offeringId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_service.not_found"));

        offering.setActive(false);
        offering.setDeletedAt(LocalDateTime.now());
        enqueueImageDeletion(offering, "BUSINESS_SERVICE_DELETED");
    }

    @Transactional
    public BusinessOfferingResponse uploadImage(
            Integer businessId,
            Integer offeringId,
            MultipartFile file,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_SERVICE_UPDATE");
        BusinessOffering offering = businessOfferingRepository
                .findByIdAndBusinessIdAndDeletedAtIsNull(offeringId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_service.not_found"));

        StoredObjectResponse uploaded = minioStorageService.uploadPublicImage(
                storageProperties.getServiceImagesBucket(),
                "businesses/" + businessId + "/services/" + offeringId + "/image",
                file);
        minioStorageService.registerRollbackCleanup(uploaded);
        enqueueImageDeletion(offering, "ASSET_REPLACED");
        offering.setImageUrl(uploaded.url());
        offering.setImageBucketName(uploaded.bucket());
        offering.setImageObjectKey(uploaded.objectKey());
        return businessOfferingMapper.toResponse(offering);
    }

    private void enqueueImageDeletion(BusinessOffering offering, String reason) {
        storageDeletionQueueService.enqueue(
                offering.getImageBucketName(),
                offering.getImageObjectKey(),
                reason,
                "BUSINESS_SERVICE_IMAGE",
                offering.getId(),
                LocalDateTime.now());
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private BusinessCatalogItem findServiceCategory(Integer businessId, Integer serviceCategoryId) {
        return businessCatalogLookupService.findActive(
                businessId,
                serviceCategoryId,
                BusinessCatalogType.SERVICE_CATEGORY);
    }

    private BusinessCatalogItem findDeviceType(Integer businessId, Integer deviceTypeId) {
        return businessCatalogLookupService.findActive(
                businessId,
                deviceTypeId,
                BusinessCatalogType.DEVICE_TYPE);
    }
}
