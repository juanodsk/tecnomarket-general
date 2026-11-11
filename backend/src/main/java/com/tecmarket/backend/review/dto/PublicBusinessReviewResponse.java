package com.tecmarket.backend.review.dto;

import java.time.LocalDateTime;

public record PublicBusinessReviewResponse(
        Integer rating,
        String comment,
        String customerDisplayName,
        LocalDateTime createdAt) {
}
