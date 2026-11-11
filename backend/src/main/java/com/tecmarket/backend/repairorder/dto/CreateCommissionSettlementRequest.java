package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Ordenes seleccionadas para liquidar las comisiones de un tecnico.")
public record CreateCommissionSettlementRequest(
        @NotNull @Schema(example = "3") Integer businessUserId,
        @NotNull @Schema(example = "2026-06-01") LocalDate periodStart,
        @NotNull @Schema(example = "2026-06-30") LocalDate periodEnd,
        @NotEmpty @Size(max = 100) @Schema(example = "[15, 18, 21]") List<@NotNull @Positive Integer> repairOrderIds,
        @Size(max = 500) @Schema(example = "Liquidacion mensual de junio.") String notes) {
}
