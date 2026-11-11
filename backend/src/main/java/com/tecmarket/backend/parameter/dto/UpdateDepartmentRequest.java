package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para actualizar un departamento.")
public record UpdateDepartmentRequest(
        @NotNull @Schema(description = "ID del pais al que pertenece el departamento.", example = "1") Integer countryId,

        @NotBlank @Size(max = 20) @Schema(description = "Codigo unico del departamento dentro del pais.", example = "ANT") String code,

        @NotBlank @Size(max = 120) @Schema(description = "Nombre del departamento.", example = "Antioquia") String name,

        @Schema(description = "Indica si el departamento esta activo.", example = "true") Boolean active) {
}
