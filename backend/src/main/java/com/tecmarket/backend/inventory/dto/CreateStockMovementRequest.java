package com.tecmarket.backend.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateStockMovementRequest(
        @NotNull
        @Schema(example = "1")
        Integer branchId,

        @NotNull
        @Schema(example = "1")
        Integer inventoryItemId,

        @NotNull
        @Schema(example = "2")
        Integer movementTypeId,

        @NotNull
        @Min(1)
        @Schema(example = "3")
        Integer quantity,

        @Schema(example = "10")
        Integer repairOrderId,

        @Schema(example = "null")
        Integer saleItemId,

        @Size(max = 500)
        @Schema(example = "Ajuste por conteo físico.")
        String notes) {
}
