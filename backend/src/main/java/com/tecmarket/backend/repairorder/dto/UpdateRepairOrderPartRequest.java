package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateRepairOrderPartRequest(
        @NotNull
        @Schema(example = "101")
        Integer inventoryItemId,

        @NotNull
        @Positive
        @Schema(example = "2")
        Integer quantity,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @Schema(example = "85000.00")
        BigDecimal unitPrice,

        @NotNull
        @Schema(example = "3")
        Integer usedByBusinessUserId) {
}
