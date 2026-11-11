package com.tecmarket.backend.repairorder.dto;

import java.math.BigDecimal;

public record TechnicianCommissionSummaryResponse(
        Integer businessUserId,
        Integer technicianUserId,
        String technicianName,
        String technicianEmail,
        BigDecimal commissionPercentage,
        Integer eligibleOrdersCount,
        BigDecimal totalCommissionBase,
        BigDecimal estimatedCommissionAmount) {
}
