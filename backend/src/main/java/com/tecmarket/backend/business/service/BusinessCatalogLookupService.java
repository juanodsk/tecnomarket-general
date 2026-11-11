package com.tecmarket.backend.business.service;

import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.business.repository.BusinessCatalogItemRepository;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BusinessCatalogLookupService {

    private final BusinessCatalogItemRepository businessCatalogItemRepository;

    public BusinessCatalogLookupService(BusinessCatalogItemRepository businessCatalogItemRepository) {
        this.businessCatalogItemRepository = businessCatalogItemRepository;
    }

    @Transactional(readOnly = true)
    public BusinessCatalogItem findActive(Integer businessId, Integer itemId, BusinessCatalogType catalogType) {
        return businessCatalogItemRepository
                .findByIdAndBusinessIdAndCatalogTypeAndDeletedAtIsNull(itemId, businessId, catalogType)
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException(errorCode(catalogType)));
    }

    @Transactional(readOnly = true)
    public BusinessCatalogItem findActiveByCode(Integer businessId, BusinessCatalogType catalogType, String code) {
        return businessCatalogItemRepository
                .findByBusinessIdAndCatalogTypeAndCodeAndDeletedAtIsNull(businessId, catalogType, normalizeCode(code))
                .filter(value -> Boolean.TRUE.equals(value.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException(errorCode(catalogType)));
    }

    private String normalizeCode(String value) {
        return value == null ? "" : value.trim().toUpperCase().replace('-', '_').replace(' ', '_');
    }

    private String errorCode(BusinessCatalogType catalogType) {
        return switch (catalogType) {
            case SERVICE_CATEGORY -> "service_category.not_found";
            case DEVICE_TYPE -> "device_type.not_found";
            case BRAND -> "brand.not_found";
            case APPOINTMENT_STATUS -> "appointment_status.not_found";
            case REPAIR_ORDER_STATUS -> "repair_order_status.not_found";
            case QUOTATION_STATUS -> "quotation_status.not_found";
            case ITEM_CATEGORY -> "item_category.not_found";
            case MOVEMENT_TYPE -> "movement_type.not_found";
            case PAYMENT_METHOD -> "payment_method.not_found";
        };
    }
}
