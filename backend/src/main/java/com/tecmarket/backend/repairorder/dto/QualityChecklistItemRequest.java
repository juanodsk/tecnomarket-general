package com.tecmarket.backend.repairorder.dto;

import com.tecmarket.backend.repairorder.entity.RepairOrderQualityCheckResult;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record QualityChecklistItemRequest(
        @NotNull
        @Schema(example = "1")
        Integer businessQualityTestId,

        @NotNull
        @Schema(example = "SI", allowableValues = {"SI", "NO", "NO_APLICA"})
        RepairOrderQualityCheckResult result,

        @Size(max = 300)
        @Schema(example = "Pantalla responde correctamente en toda el área táctil.")
        String notes) {
}
