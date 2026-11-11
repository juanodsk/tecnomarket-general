package com.tecmarket.backend.review.dto;

import java.math.BigDecimal;

public record PublicBusinessDetailResponse(
        Integer id,
        String name,
        String legalName,
        String description,
        Integer cityId,
        String cityName,
        String address,
        String phone,
        String email,
        String logo,
        String coverImage,
        BigDecimal ratingAverage,
        Integer ratingCount) {
}
