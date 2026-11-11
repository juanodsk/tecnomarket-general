package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateBusinessOfferingRequest(
        @NotNull
        @Schema(example = "1")
        Integer serviceCategoryId,

        @NotNull
        @Schema(example = "1")
        Integer deviceTypeId,

        @NotBlank
        @Size(max = 50)
        @Schema(example = "Cambio de pantalla OLED Premium")
        String name,

        @NotBlank
        @Size(max = 200)
        @Schema(example = "Reemplazo de pantalla con garantía de instalación.")
        String description,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @Schema(example = "390000.00")
        BigDecimal basePrice,

        @NotNull
        @Positive
        @Schema(example = "150")
        Integer estimatedTimeMinutes,

        @Schema(example = "true")
        Boolean active) {
}
