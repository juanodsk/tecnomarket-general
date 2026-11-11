package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para actualizar un modulo de la aplicacion.")
public record UpdateAppModuleRequest(
        @NotBlank @Size(max = 100) @Schema(description = "Nombre visible del modulo.", example = "Inventario y stock") String name,

        @Schema(description = "Indica si el modulo esta activo.", example = "true") Boolean active) {
}
