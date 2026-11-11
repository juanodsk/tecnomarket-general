package com.tecmarket.backend.quotation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record QuotationItemResponse(
        @Schema(example = "1")
        Integer id,

        @Schema(example = "Cambio de batería original")
        String description,

        @Schema(example = "1")
        Integer quantity,

        @Schema(example = "180000.00")
        BigDecimal unitPrice,

        @Schema(example = "180000.00")
        BigDecimal totalPrice) {
}
