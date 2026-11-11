package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para actualizar un rol del sistema.")
public record UpdateRoleRequest(
        @NotBlank @Size(max = 100) @Schema(description = "Nombre visible del rol.", example = "Tecnico lider") String name,

        @NotBlank @Size(max = 255) @Schema(description = "Descripcion funcional del rol.", example = "Usuario encargado de supervisar diagnosticos y reparaciones.") String description,

        @NotBlank @Size(max = 30) @Schema(description = "Alcance del rol dentro del sistema.", example = "BUSINESS", allowableValues = {
                "GLOBAL", "BUSINESS" }) String scope,

        @Schema(description = "Indica si el rol esta activo.", example = "true") Boolean active) {
}
