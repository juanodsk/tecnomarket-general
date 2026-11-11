package com.tecmarket.backend.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleResponse(
        @Schema(example = "1")
        Integer id,
        @Schema(example = "2")
        Integer businessId,
        @Schema(example = "1")
        Integer branchId,
        @Schema(example = "Sede principal")
        String branchName,
        @Schema(example = "2")
        Integer customerId,
        @Schema(example = "Juan Perez")
        String customerName,
        @Schema(example = "4")
        Integer soldByBusinessUserId,
        @Schema(example = "Carlos Gomez")
        String soldByBusinessUserName,
        @Schema(example = "1")
        Integer paymentMethodId,
        @Schema(example = "CASH")
        String paymentMethodCode,
        @Schema(example = "Efectivo")
        String paymentMethodName,
        @Schema(example = "240000.00")
        BigDecimal totalAmount,
        @Schema(example = "2026-05-16T12:45:00")
        LocalDateTime createdAt,
        List<SaleItemResponse> items) {
}
