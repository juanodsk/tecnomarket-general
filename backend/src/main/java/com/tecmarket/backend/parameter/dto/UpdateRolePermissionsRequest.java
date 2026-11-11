package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Lista de permisos que deben quedar asignados al rol.")
public record UpdateRolePermissionsRequest(
        @NotNull @Size(max = 200)
        @Schema(description = "IDs de permisos activos que se asignaran al rol. Enviar lista vacia para quitar todos.", example = "[1,2,3]")
        List<@NotNull @Positive Integer> permissionIds) {
}
