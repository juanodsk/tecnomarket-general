package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateBusinessOfferingRequest(
        @NotNull
        @Schema(example = "1")
        Integer serviceCategoryId,

        @NotNull
        @Schema(example = "1")
        Integer deviceTypeId,

        @NotBlank
        @Size(max = 50)
        @Schema(example = "Cambio de pantalla OLED")
        String name,

        @NotBlank
        @Size(max = 200)
        @Schema(example = "Reemplazo de pantalla con repuesto compatible y prueba de display.")
        String description,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @Schema(example = "350000.00")
        BigDecimal basePrice,

        @NotNull
        @Positive
        @Schema(example = "120")
        Integer estimatedTimeMinutes) {
}
