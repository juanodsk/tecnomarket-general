package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para actualizar un permiso del sistema.")
public record UpdatePermissionRequest(
        @NotNull @Schema(description = "Identificador del modulo al que pertenece el permiso.", example = "1") Integer moduleId,

        @NotBlank @Size(max = 120) @Schema(description = "Nombre visible del permiso.", example = "Ver inventario") String name,

        @NotBlank @Size(max = 255) @Schema(description = "Descripcion funcional del permiso.", example = "Permite consultar productos, repuestos y accesorios del inventario.") String description,

        @Schema(description = "Indica si el permiso esta activo.", example = "true") Boolean active) {
}
