package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para crear un rol del sistema.")
public record CreateRoleRequest(
        @NotBlank @Size(max = 50) @Schema(description = "Codigo unico del rol. Se almacena en mayusculas.", example = "TECHNICIAN") String code,

        @NotBlank @Size(max = 100) @Schema(description = "Nombre visible del rol.", example = "Tecnico") String name,

        @NotBlank @Size(max = 255) @Schema(description = "Descripcion funcional del rol.", example = "Usuario encargado de diagnosticar y reparar equipos.") String description,

        @NotBlank @Size(max = 30) @Schema(description = "Alcance del rol dentro del sistema.", example = "BUSINESS", allowableValues = {
                "GLOBAL", "BUSINESS" }) String scope) {
}
