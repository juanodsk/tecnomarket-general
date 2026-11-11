package com.tecmarket.backend.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateSaleItemRequest(
        @NotNull
        @Schema(example = "1")
        Integer inventoryItemId,

        @NotNull
        @Min(1)
        @Schema(example = "2")
        Integer quantity) {
}
