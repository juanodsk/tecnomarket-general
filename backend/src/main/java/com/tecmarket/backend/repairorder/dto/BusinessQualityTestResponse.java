package com.tecmarket.backend.repairorder.dto;

public record BusinessQualityTestResponse(
        Integer id,
        Integer businessId,
        Integer deviceTypeId,
        String deviceTypeCode,
        String deviceTypeName,
        String code,
        String name,
        String description,
        Boolean required,
        Integer sortOrder,
        Boolean active) {
}
