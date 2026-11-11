package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBusinessQualityTestRequest(
        @NotNull
        @Schema(example = "1")
        Integer deviceTypeId,

        @NotBlank
        @Size(max = 30)
        @Schema(example = "SCREEN_OK")
        String code,

        @NotBlank
        @Size(max = 50)
        @Schema(example = "Pantalla funciona")
        String name,

        @NotBlank
        @Size(max = 200)
        @Schema(example = "Valida touch, brillo y display sin líneas.")
        String description,

        @Schema(example = "true")
        Boolean required,

        @NotNull
        @Min(1)
        @Schema(example = "1")
        Integer sortOrder) {
}
