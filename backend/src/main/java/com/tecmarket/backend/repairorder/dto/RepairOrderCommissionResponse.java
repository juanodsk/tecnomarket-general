package com.tecmarket.backend.repairorder.dto;

import com.tecmarket.backend.repairorder.entity.RepairOrderCommissionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RepairOrderCommissionResponse(
        Integer id,
        Integer businessId,
        Integer settlementId,
        String settlementCode,
        Integer repairOrderId,
        String orderNumber,
        Integer businessUserId,
        Integer technicianUserId,
        String technicianName,
        String technicianEmail,
        BigDecimal orderTotal,
        BigDecimal partsTotal,
        BigDecimal commissionBase,
        BigDecimal commissionPercentage,
        BigDecimal commissionAmount,
        RepairOrderCommissionStatus status,
        LocalDateTime generatedAt,
        LocalDateTime approvedAt,
        LocalDateTime paidAt) {
}
