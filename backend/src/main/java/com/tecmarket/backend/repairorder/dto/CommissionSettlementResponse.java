package com.tecmarket.backend.repairorder.dto;

import com.tecmarket.backend.repairorder.entity.CommissionSettlementStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CommissionSettlementResponse(
        Integer id,
        String settlementCode,
        Integer businessId,
        Integer businessUserId,
        Integer technicianUserId,
        String technicianName,
        String technicianEmail,
        LocalDate periodStart,
        LocalDate periodEnd,
        Integer ordersCount,
        BigDecimal totalAmount,
        CommissionSettlementStatus status,
        String notes,
        LocalDateTime generatedAt,
        LocalDateTime approvedAt,
        LocalDateTime paidAt,
        List<RepairOrderCommissionResponse> commissions) {
}
