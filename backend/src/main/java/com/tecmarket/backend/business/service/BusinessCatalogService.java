package com.tecmarket.backend.business.service;

import com.tecmarket.backend.business.dto.BusinessCatalogItemResponse;
import com.tecmarket.backend.business.dto.CreateBusinessCatalogItemRequest;
import com.tecmarket.backend.business.dto.UpdateBusinessCatalogItemRequest;
import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.business.repository.BusinessCatalogItemRepository;
import com.tecmarket.backend.business.repository.BusinessRepository;
import com.tecmarket.backend.business.security.BusinessAccessGuard;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BusinessCatalogService {

    private final BusinessCatalogItemRepository businessCatalogItemRepository;
    private final BusinessRepository businessRepository;
    private final BusinessAccessGuard businessAccessGuard;

    public BusinessCatalogService(
            BusinessCatalogItemRepository businessCatalogItemRepository,
            BusinessRepository businessRepository,
            BusinessAccessGuard businessAccessGuard) {
        this.businessCatalogItemRepository = businessCatalogItemRepository;
        this.businessRepository = businessRepository;
        this.businessAccessGuard = businessAccessGuard;
    }

    @Transactional(readOnly = true)
    public List<BusinessCatalogItemResponse> findAll(Integer businessId, String type, String subject) {
        businessAccessGuard.assertHasBusinessPermission(
                businessId,
                subject,
                "BUSINESS_CATALOG_VIEW",
                "BUSINESS_CATALOG_MANAGE");
        BusinessCatalogType catalogType = parseCatalogType(type);

        return businessCatalogItemRepository
                .findByBusinessIdAndCatalogTypeAndDeletedAtIsNullOrderBySortOrderAscNameAscIdAsc(businessId, catalogType)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BusinessCatalogItemResponse findById(Integer businessId, String type, Integer itemId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(
                businessId,
                subject,
                "BUSINESS_CATALOG_VIEW",
                "BUSINESS_CATALOG_MANAGE");
        BusinessCatalogType catalogType = parseCatalogType(type);

        return businessCatalogItemRepository
                .findByIdAndBusinessIdAndCatalogTypeAndDeletedAtIsNull(itemId, businessId, catalogType)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("business_catalog.not_found"));
    }

    @Transactional
    public BusinessCatalogItemResponse create(
            Integer businessId,
            String type,
            CreateBusinessCatalogItemRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_CATALOG_MANAGE");
        Business business = findBusiness(businessId);
        BusinessCatalogType catalogType = parseCatalogType(type);
        String code = normalizeCode(request.code());

        if (businessCatalogItemRepository.existsByBusinessIdAndCatalogTypeAndCodeAndDeletedAtIsNull(
                businessId,
                catalogType,
                code)) {
            throw new ConflictException("business_catalog.code.exists");
        }

        BusinessCatalogItem item = new BusinessCatalogItem();
        item.setBusiness(business);
        item.setCatalogType(catalogType);
        item.setCode(code);
        item.setName(request.name().trim());
        item.setDescription(normalizeNullable(request.description()));
        item.setIcon(normalizeNullable(request.icon()));
        item.setSortOrder(request.sortOrder());
        item.setProtectedItem(false);
        item.setActive(request.active() == null || request.active());

        return toResponse(businessCatalogItemRepository.save(item));
    }

    @Transactional
    public BusinessCatalogItemResponse update(
            Integer businessId,
            String type,
            Integer itemId,
            UpdateBusinessCatalogItemRequest request,
            String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_CATALOG_MANAGE");
        BusinessCatalogType catalogType = parseCatalogType(type);

        BusinessCatalogItem item = businessCatalogItemRepository
                .findByIdAndBusinessIdAndCatalogTypeAndDeletedAtIsNull(itemId, businessId, catalogType)
                .orElseThrow(() -> new ResourceNotFoundException("business_catalog.not_found"));

        if (Boolean.TRUE.equals(item.getProtectedItem()) && Boolean.FALSE.equals(request.active())) {
            throw new ConflictException("business_catalog.protected");
        }

        item.setName(request.name().trim());
        item.setDescription(normalizeNullable(request.description()));
        item.setIcon(normalizeNullable(request.icon()));
        item.setSortOrder(request.sortOrder());
        if (request.active() != null) {
            item.setActive(request.active());
        }

        return toResponse(item);
    }

    @Transactional
    public void delete(Integer businessId, String type, Integer itemId, String subject) {
        businessAccessGuard.assertHasBusinessPermission(businessId, subject, "BUSINESS_CATALOG_MANAGE");
        BusinessCatalogType catalogType = parseCatalogType(type);

        BusinessCatalogItem item = businessCatalogItemRepository
                .findByIdAndBusinessIdAndCatalogTypeAndDeletedAtIsNull(itemId, businessId, catalogType)
                .orElseThrow(() -> new ResourceNotFoundException("business_catalog.not_found"));

        if (Boolean.TRUE.equals(item.getProtectedItem())) {
            throw new ConflictException("business_catalog.protected");
        }

        item.setActive(false);
        item.setDeletedAt(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public BusinessCatalogItem findActiveByCode(Integer businessId, BusinessCatalogType catalogType, String code) {
        return businessCatalogItemRepository
                .findByBusinessIdAndCatalogTypeAndCodeAndDeletedAtIsNull(businessId, catalogType, normalizeCode(code))
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("business_catalog.not_found"));
    }

    private Business findBusiness(Integer businessId) {
        return businessRepository.findByIdAndDeletedAtIsNull(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("business.not_found"));
    }

    @Transactional
    public BusinessCatalogItem upsertSeedItem(
            Business business,
            BusinessCatalogType catalogType,
            String code,
            String name,
            String description,
            String icon,
            Integer sortOrder,
            boolean protectedItem,
            Boolean active) {
        String normalizedCode = normalizeCode(code);
        BusinessCatalogItem item = businessCatalogItemRepository
                .findByBusinessIdAndCatalogTypeAndCodeAndDeletedAtIsNull(business.getId(), catalogType, normalizedCode)
                .orElseGet(BusinessCatalogItem::new);

        item.setBusiness(business);
        item.setCatalogType(catalogType);
        item.setCode(normalizedCode);
        item.setName(StringUtils.hasText(name) ? name.trim() : normalizedCode);
        item.setDescription(normalizeNullable(description));
        item.setIcon(normalizeNullable(icon));
        item.setSortOrder(sortOrder);
        item.setProtectedItem(protectedItem);
        item.setActive(active == null || active);
        item.setDeletedAt(null);
        return businessCatalogItemRepository.save(item);
    }

    private BusinessCatalogType parseCatalogType(String type) {
        try {
            return BusinessCatalogType.valueOf(normalizeCode(type));
        } catch (IllegalArgumentException exception) {
            throw new ResourceNotFoundException("business_catalog.type.not_found");
        }
    }

    private String normalizeCode(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        return value.trim()
                .toUpperCase()
                .replace('-', '_')
                .replace(' ', '_')
                .replaceAll("[^A-Z0-9_]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
    }

    private String normalizeNullable(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private BusinessCatalogItemResponse toResponse(BusinessCatalogItem item) {
        return new BusinessCatalogItemResponse(
                item.getId(),
                item.getBusiness().getId(),
                item.getCatalogType(),
                item.getCode(),
                item.getName(),
                item.getDescription(),
                item.getIcon(),
                item.getSortOrder(),
                item.getProtectedItem(),
                item.getActive(),
                item.getCreatedAt(),
                item.getUpdatedAt());
    }
}
