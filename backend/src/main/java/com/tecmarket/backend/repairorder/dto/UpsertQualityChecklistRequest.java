package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpsertQualityChecklistRequest(
        @Schema(example = "null", description = "Opcional mientras no exista el módulo de repair_order completo.")
        @Positive
        Integer repairOrderId,

        @NotEmpty
        @Size(max = 100)
        @Valid
        List<QualityChecklistItemRequest> items) {
}
