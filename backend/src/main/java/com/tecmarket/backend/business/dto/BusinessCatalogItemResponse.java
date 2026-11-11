package com.tecmarket.backend.business.dto;

import com.tecmarket.backend.business.entity.BusinessCatalogType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Elemento de catalogo configurable por negocio.")
public record BusinessCatalogItemResponse(
        @Schema(description = "ID del elemento de catalogo.", example = "1") Integer id,
        @Schema(description = "ID del negocio propietario.", example = "1") Integer businessId,
        @Schema(description = "Tipo de catalogo del negocio.", example = "SERVICE_CATEGORY") BusinessCatalogType catalogType,
        @Schema(description = "Codigo interno del elemento.", example = "SCREEN_REPAIR") String code,
        @Schema(description = "Nombre visible.", example = "Cambio de pantalla") String name,
        @Schema(description = "Descripcion del elemento.", example = "Servicios asociados a cambio de pantalla.") String description,
        @Schema(description = "Icono sugerido para UI.", example = "monitor-smartphone") String icon,
        @Schema(description = "Orden visual dentro del catalogo.", example = "1") Integer sortOrder,
        @Schema(description = "Indica si el elemento es requerido por flujos internos.", example = "true") Boolean protectedItem,
        @Schema(description = "Estado activo.", example = "true") Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
