package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para actualizar un elemento de catalogo por negocio.")
public record UpdateBusinessCatalogItemRequest(
        @NotBlank @Size(max = 150) @Schema(description = "Nombre visible del elemento.", example = "Cambio de pantalla premium") String name,
        @Size(max = 500) @Schema(description = "Descripcion del elemento.", example = "Servicio especializado para cambio de pantalla.") String description,
        @Size(max = 255) @Schema(description = "Icono sugerido para UI.", example = "monitor-smartphone") String icon,
        @Schema(description = "Orden visual dentro del catalogo.", example = "2") Integer sortOrder,
        @Schema(description = "Estado activo.", example = "true") Boolean active) {
}
