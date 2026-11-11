package com.tecmarket.backend.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record SaleItemResponse(
        @Schema(example = "1")
        Integer id,
        @Schema(example = "1")
        Integer inventoryItemId,
        @Schema(example = "BAT-IP11-ORI")
        String inventoryItemSku,
        @Schema(example = "Batería iPhone 11")
        String inventoryItemName,
        @Schema(example = "2")
        Integer quantity,
        @Schema(example = "85000.00")
        BigDecimal unitCost,
        @Schema(example = "120000.00")
        BigDecimal unitPrice,
        @Schema(example = "170000.00")
        BigDecimal totalCost,
        @Schema(example = "240000.00")
        BigDecimal totalPrice) {
}
