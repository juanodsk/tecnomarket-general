package com.tecmarket.backend.quotation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record QuotationResponse(
        @Schema(example = "1")
        Integer id,

        @Schema(example = "2")
        Integer businessId,

        @Schema(example = "10")
        Integer repairOrderId,

        @Schema(example = "5")
        Integer customerId,

        @Schema(example = "Juan Perez")
        String customerName,

        @Schema(example = "DRAFT")
        String statusCode,

        @Schema(example = "Borrador")
        String statusName,

        @Schema(example = "245000.00")
        BigDecimal totalAmount,

        @Schema(example = "Cotización válida por 5 días calendario.")
        String notes,

        @Schema(example = "2026-05-21T23:59:00")
        LocalDateTime expiresAt,

        @Schema(example = "2026-05-16T14:30:00")
        LocalDateTime createdAt,

        List<QuotationItemResponse> items) {
}
