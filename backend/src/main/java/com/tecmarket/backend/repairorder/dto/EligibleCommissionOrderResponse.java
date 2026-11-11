package com.tecmarket.backend.repairorder.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EligibleCommissionOrderResponse(
        Integer repairOrderId,
        String orderNumber,
        Integer businessUserId,
        String technicianName,
        LocalDateTime receivedAt,
        BigDecimal orderTotal,
        BigDecimal partsTotal,
        BigDecimal commissionBase,
        BigDecimal commissionPercentage,
        BigDecimal commissionAmount) {
}
