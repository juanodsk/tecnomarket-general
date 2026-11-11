package com.tecmarket.backend.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryItemResponse(
        @Schema(example = "1")
        Integer id,
        @Schema(example = "2")
        Integer businessId,
        @Schema(example = "1")
        Integer branchId,
        @Schema(example = "Sede principal")
        String branchName,
        @Schema(example = "Batería iPhone 11")
        String name,
        @Schema(example = "1")
        Integer itemCategoryId,
        @Schema(example = "SPARE_PART")
        String itemCategoryCode,
        @Schema(example = "Repuesto")
        String itemCategoryName,
        @Schema(example = "Batería original con garantía de 6 meses.")
        String description,
        @Schema(example = "BAT-IP11-ORI")
        String sku,
        @Schema(example = "25")
        Integer quantity,
        @Schema(example = "5")
        Integer minimumStock,
        @Schema(example = "85000.00")
        BigDecimal unitCost,
        @Schema(example = "120000.00")
        BigDecimal salePrice,
        @Schema(example = "true")
        Boolean active,
        @Schema(example = "2026-05-16T12:15:00")
        LocalDateTime createdAt) {
}
