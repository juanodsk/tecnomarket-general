package com.tecmarket.backend.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Plan de suscripcion SaaS disponible para negocios.")
public record SubscriptionPlanResponse(
        @Schema(example = "1") Integer id,
        @Schema(example = "PRO") String code,
        @Schema(example = "Pro") String name,
        @Schema(example = "99000.00") BigDecimal monthlyPrice,
        @Schema(example = "999000.00") BigDecimal yearlyPrice,
        @Schema(example = "4") Integer includedUsers,
        @Schema(example = "1") Integer maxBusinesses,
        @Schema(example = "2") Integer maxBranches,
        @Schema(example = "[\"Inventario\", \"Cotizaciones\", \"Reportes basicos\"]") String features,
        @Schema(example = "true") Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
