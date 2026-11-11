package com.tecmarket.backend.review.dto;

import java.time.LocalDateTime;

public record BusinessReviewResponse(
        Integer id,
        Integer businessId,
        Integer customerId,
        String customerName,
        String customerSurname,
        String customerEmail,
        Integer repairOrderId,
        String repairOrderNumber,
        Integer rating,
        String comment,
        LocalDateTime createdAt) {
}
