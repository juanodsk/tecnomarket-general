package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para crear una ciudad.")
public record CreateCityRequest(
        @NotNull @Schema(description = "ID del departamento al que pertenece la ciudad.", example = "1") Integer departmentId,

        @NotBlank @Size(max = 120) @Schema(description = "Nombre de la ciudad.", example = "Bogota") String name) {
}
