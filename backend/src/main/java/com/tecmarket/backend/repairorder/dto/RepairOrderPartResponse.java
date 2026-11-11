package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RepairOrderPartResponse(
        @Schema(example = "1")
        Integer id,

        @Schema(example = "10")
        Integer repairOrderId,

        @Schema(example = "101")
        Integer inventoryItemId,

        @Schema(example = "Batería iPhone 11")
        String inventoryItemName,

        @Schema(example = "BAT-IP11-001")
        String inventoryItemSku,

        @Schema(example = "SPARE_PART")
        String inventoryItemCategoryCode,

        @Schema(example = "Repuesto")
        String inventoryItemCategoryName,

        @Schema(example = "1")
        Integer quantity,

        @Schema(example = "85000.00")
        BigDecimal unitPrice,

        @Schema(example = "85000.00")
        BigDecimal totalPrice,

        @Schema(example = "3")
        Integer usedByBusinessUserId,

        @Schema(example = "tecnico@negocio.com")
        String usedByEmail,

        @Schema(example = "Carlos Gomez")
        String usedByName,

        @Schema(example = "2026-05-16T11:25:00")
        LocalDateTime createdAt) {
}
