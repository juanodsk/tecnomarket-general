package com.tecmarket.backend.repairorder.service;

import com.tecmarket.backend.repairorder.dto.BusinessQualityTestResponse;
import com.tecmarket.backend.repairorder.dto.CreateBusinessQualityTestRequest;
import com.tecmarket.backend.repairorder.dto.UpdateBusinessQualityTestRequest;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.repairorder.entity.BusinessQualityTest;
import com.tecmarket.backend.repairorder.mapper.BusinessQualityTestMapper;
import com.tecmarket.backend.repairorder.repository.BusinessQualityTestRepository;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.common.pagination.PageRequestFactory;
import com.tecmarket.backend.common.pagination.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import com.tecmarket.backend.business.service.BusinessCatalogLookupService;
@Service
public class BusinessQualityTestService {

    private final BusinessQualityTestRepository businessQualityTestRepository;
    private final BusinessRepository businessRepository;
    private final BusinessCatalogLookupService businessCatalogLookupService;
    private final BusinessQualityTestMapper businessQualityTestMapper;
    private final BusinessAccessGuard businessAccessGuard;

    public BusinessQualityTestService(
            BusinessQualityTestRepository businessQualityTestRepository,
            BusinessRepository businessRepository,
            BusinessCatalogLookupService businessCatalogLookupService,
            BusinessQualityTestMapper businessQualityTestMapper,
            BusinessAccessGuard businessAccessGuard) {
        this.businessQualityTestRepository = businessQualityTestRepository;
        this.businessRepository = businessRepository;
        this.businessCatalogLookupService = businessCatalogLookupService;
        this.businessQualityTestMapper = businessQualityTestMapper;
        this.businessAccessGuard = businessAccessGuard;
    }

    @Transactional(readOnly = true)
    public PageResponse<BusinessQualityTestResponse> findAllByBusiness(
            Integer businessId, Integer page, Integer size, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_QUALITY_TEST_VIEW");
        findBusiness(businessId);
        return PageResponse.from(businessQualityTestRepository
                .findByBusinessIdAndDeletedAtIsNullOrderByDeviceTypeIdAscSortOrderAscNameAsc(
                        businessId, PageRequestFactory.of(page, size))
                .map(businessQualityTestMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public BusinessQualityTestResponse findById(Integer businessId, Integer qualityTestId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_QUALITY_TEST_VIEW");
        BusinessQualityTest qualityTest = businessQualityTestRepository
                .findByIdAndBusinessIdAndDeletedAtIsNull(qualityTestId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_quality_test.not_found"));
        return businessQualityTestMapper.toResponse(qualityTest);
    }

    @Transactional
    public BusinessQualityTestResponse create(
            Integer businessId,
            CreateBusinessQualityTestRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_QUALITY_TEST_CREATE");
        Business business = findBusiness(businessId);
        BusinessCatalogItem deviceType = findDeviceType(businessId, request.deviceTypeId());
        String normalizedCode = normalizeCode(request.code());

        if (businessQualityTestRepository.existsByBusinessIdAndDeviceTypeIdAndCodeAndDeletedAtIsNull(
                businessId,
                deviceType.getId(),
                normalizedCode)) {
            throw new ConflictException("business_quality_test.code.exists");
        }

        BusinessQualityTest qualityTest = new BusinessQualityTest();
        qualityTest.setBusiness(business);
        qualityTest.setDeviceType(deviceType);
        qualityTest.setCode(normalizedCode);
        qualityTest.setName(request.name().trim());
        qualityTest.setDescription(request.description().trim());
        qualityTest.setRequired(request.required() != null ? request.required() : false);
        qualityTest.setSortOrder(request.sortOrder());
        qualityTest.setActive(true);

        return businessQualityTestMapper.toResponse(businessQualityTestRepository.save(qualityTest));
    }

    @Transactional
    public BusinessQualityTestResponse update(
            Integer businessId,
            Integer qualityTestId,
            UpdateBusinessQualityTestRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_QUALITY_TEST_UPDATE");
        BusinessQualityTest qualityTest = businessQualityTestRepository
                .findByIdAndBusinessIdAndDeletedAtIsNull(qualityTestId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_quality_test.not_found"));

        BusinessCatalogItem deviceType = findDeviceType(businessId, request.deviceTypeId());
        String normalizedCode = normalizeCode(request.code());

        if (businessQualityTestRepository.existsByBusinessIdAndDeviceTypeIdAndCodeAndDeletedAtIsNullAndIdNot(
                businessId,
                deviceType.getId(),
                normalizedCode,
                qualityTestId)) {
            throw new ConflictException("business_quality_test.code.exists");
        }

        qualityTest.setDeviceType(deviceType);
        qualityTest.setCode(normalizedCode);
        qualityTest.setName(request.name().trim());
        qualityTest.setDescription(request.description().trim());
        qualityTest.setRequired(request.required() != null ? request.required() : false);
        qualityTest.setSortOrder(request.sortOrder());

        if (request.active() != null) {
            qualityTest.setActive(request.active());
            qualityTest.setDeletedAt(Boolean.FALSE.equals(request.active()) ? LocalDateTime.now() : null);
        }

        return businessQualityTestMapper.toResponse(qualityTest);
    }

    @Transactional
    public void delete(Integer businessId, Integer qualityTestId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_QUALITY_TEST_UPDATE");
        BusinessQualityTest qualityTest = businessQualityTestRepository
                .findByIdAndBusinessIdAndDeletedAtIsNull(qualityTestId, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business_quality_test.not_found"));

        qualityTest.setActive(false);
        qualityTest.setDeletedAt(LocalDateTime.now());
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    private BusinessCatalogItem findDeviceType(Integer businessId, Integer deviceTypeId) {
        return businessCatalogLookupService.findActive(
                businessId,
                deviceTypeId,
                BusinessCatalogType.DEVICE_TYPE);
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }
}
