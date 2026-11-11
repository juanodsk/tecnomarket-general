package com.tecmarket.backend.review.dto;

import java.math.BigDecimal;

public record PublicBusinessResponse(
        Integer id,
        String name,
        String description,
        Integer cityId,
        String cityName,
        String logo,
        String coverImage,
        BigDecimal ratingAverage,
        Integer ratingCount) {
}
