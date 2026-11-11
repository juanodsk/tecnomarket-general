package com.tecmarket.backend.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record StockMovementResponse(
        @Schema(example = "1")
        Integer id,
        @Schema(example = "1")
        Integer branchId,
        @Schema(example = "Sede principal")
        String branchName,
        @Schema(example = "1")
        Integer inventoryItemId,
        @Schema(example = "BAT-IP11-ORI")
        String inventoryItemSku,
        @Schema(example = "Batería iPhone 11")
        String inventoryItemName,
        @Schema(example = "2")
        Integer movementTypeId,
        @Schema(example = "OUT")
        String movementTypeCode,
        @Schema(example = "Salida")
        String movementTypeName,
        @Schema(example = "3")
        Integer quantity,
        @Schema(example = "4")
        Integer businessUserId,
        @Schema(example = "Carlos Gomez")
        String businessUserName,
        @Schema(example = "10")
        Integer repairOrderId,
        @Schema(example = "5")
        Integer saleItemId,
        @Schema(example = "Ajuste por conteo físico.")
        String notes,
        @Schema(example = "2026-05-16T12:30:00")
        LocalDateTime createdAt) {
}
