package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para actualizar una ciudad.")
public record UpdateCityRequest(
        @NotNull @Schema(description = "ID del departamento al que pertenece la ciudad.", example = "1") Integer departmentId,

        @NotBlank @Size(max = 120) @Schema(description = "Nombre de la ciudad.", example = "Bogota") String name,

        @Schema(description = "Indica si la ciudad esta activa.", example = "true") Boolean active) {
}
