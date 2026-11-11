package com.tecmarket.backend.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Datos para crear un plan de suscripcion SaaS.")
public record CreateSubscriptionPlanRequest(
        @NotBlank @Size(max = 50) @Schema(example = "PRO") String code,
        @NotBlank @Size(max = 120) @Schema(example = "Pro") String name,
        @NotNull @DecimalMin(value = "0.00") @Schema(example = "99000.00") BigDecimal monthlyPrice,
        @NotNull @DecimalMin(value = "0.00") @Schema(example = "999000.00") BigDecimal yearlyPrice,
        @NotNull @Min(1) @Schema(example = "4") Integer includedUsers,
        @NotNull @Min(1) @Schema(example = "1") Integer maxBusinesses,
        @NotNull @Min(1) @Schema(example = "2") Integer maxBranches,
        @NotBlank @Schema(example = "[\"Inventario\", \"Cotizaciones\", \"Reportes basicos\"]") String features,
        @Schema(example = "true") Boolean active) {
}
