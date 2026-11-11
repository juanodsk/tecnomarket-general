package com.tecmarket.backend.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBusinessReviewRequest(
        @NotNull
        @Schema(example = "1")
        Integer repairOrderId,

        @NotNull
        @Min(1)
        @Max(5)
        @Schema(example = "5")
        Integer rating,

        @Size(max = 500)
        @Schema(example = "Excelente atención, diagnóstico claro y entrega a tiempo.")
        String comment) {
}
