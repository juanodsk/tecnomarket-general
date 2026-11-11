package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para actualizar un tipo de documento.")
public record UpdateDocumentTypeRequest(
        @NotBlank @Size(max = 50) @Schema(description = "Nombre visible del tipo de documento.", example = "Cedula de ciudadania") String name,

        @NotBlank @Size(max = 100) @Schema(description = "Descripcion funcional del tipo de documento.", example = "Documento de identidad colombiano") String description,

        @NotNull @Schema(description = "Indica si aplica para personas naturales.", example = "true") Boolean appliesToPerson,

        @NotNull @Schema(description = "Indica si aplica para empresas.", example = "false") Boolean appliesToCompany,

        @Schema(description = "Indica si el tipo de documento esta activo.", example = "true") Boolean active) {
}
