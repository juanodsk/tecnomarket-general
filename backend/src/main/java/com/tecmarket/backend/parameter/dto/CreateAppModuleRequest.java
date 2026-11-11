package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para crear un modulo de la aplicacion.")
public record CreateAppModuleRequest(
        @NotBlank @Size(max = 50) @Schema(description = "Codigo unico del modulo. Se almacena en mayusculas.", example = "INVENTORY") String code,

        @NotBlank @Size(max = 100) @Schema(description = "Nombre visible del modulo.", example = "Inventario") String name) {
}
