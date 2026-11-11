package com.tecmarket.backend.business.dto;

import java.math.BigDecimal;

public record BusinessOfferingResponse(
        Integer id,
        Integer businessId,
        Integer serviceCategoryId,
        String serviceCategoryCode,
        String serviceCategoryName,
        Integer deviceTypeId,
        String deviceTypeCode,
        String deviceTypeName,
        String name,
        String description,
        BigDecimal basePrice,
        Integer estimatedTimeMinutes,
        String imageUrl,
        Boolean active) {
}
