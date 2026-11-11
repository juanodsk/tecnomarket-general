package com.tecmarket.backend.business.dto;

import java.math.BigDecimal;

public record BusinessResponse(
        Integer id,
        String documentTypeCode,
        String documentNumber,
        String name,
        String legalName,
        String email,
        String phone,
        String address,
        String description,
        Integer cityId,
        String cityName,
        String logo,
        String coverImage,
        BigDecimal ratingAverage,
        Integer ratingCount,
        Boolean active) {
}
