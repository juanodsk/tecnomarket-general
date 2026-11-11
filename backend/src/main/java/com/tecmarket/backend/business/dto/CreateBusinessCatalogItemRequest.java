package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear un elemento de catalogo por negocio.")
public record CreateBusinessCatalogItemRequest(
        @NotBlank @Size(max = 50) @Schema(description = "Codigo interno unico por negocio y tipo de catalogo.", example = "SCREEN_REPAIR") String code,
        @NotBlank @Size(max = 150) @Schema(description = "Nombre visible del elemento.", example = "Cambio de pantalla") String name,
        @Size(max = 500) @Schema(description = "Descripcion del elemento.", example = "Servicios asociados a cambio de pantalla.") String description,
        @Size(max = 255) @Schema(description = "Icono sugerido para UI.", example = "monitor-smartphone") String icon,
        @Schema(description = "Orden visual dentro del catalogo.", example = "1") Integer sortOrder,
        @Schema(description = "Estado activo inicial.", example = "true") Boolean active) {
}
