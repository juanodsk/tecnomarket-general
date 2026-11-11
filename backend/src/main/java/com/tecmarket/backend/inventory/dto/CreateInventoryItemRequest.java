package com.tecmarket.backend.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateInventoryItemRequest(
        @NotNull
        @Schema(example = "1")
        Integer branchId,

        @NotBlank
        @Size(max = 150)
        @Schema(example = "Batería iPhone 11")
        String name,

        @NotNull
        @Schema(example = "1")
        Integer itemCategoryId,

        @Size(max = 500)
        @Schema(example = "Batería original con garantía de 6 meses.")
        String description,

        @NotBlank
        @Size(max = 80)
        @Schema(example = "BAT-IP11-ORI")
        String sku,

        @NotNull
        @Min(0)
        @Schema(example = "25")
        Integer quantity,

        @NotNull
        @Min(0)
        @Schema(example = "5")
        Integer minimumStock,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @Schema(example = "85000.00")
        BigDecimal unitCost,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @Schema(example = "120000.00")
        BigDecimal salePrice) {
}
