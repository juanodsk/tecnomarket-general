package com.tecmarket.backend.quotation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateQuotationItemRequest(
        @NotBlank
        @Size(max = 300)
        @Schema(example = "Cambio de batería original")
        String description,

        @NotNull
        @Positive
        @Schema(example = "1")
        Integer quantity,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @Schema(example = "180000.00")
        BigDecimal unitPrice) {
}
