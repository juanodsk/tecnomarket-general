package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Lista de roles que deben quedar asignados al usuario.")
public record UpdateUserRolesRequest(
        @NotNull @Size(max = 20)
        @Schema(description = "IDs de roles activos que se asignaran al usuario. Enviar lista vacia para quitar todos.", example = "[1,2,3]")
        List<@NotNull @Positive Integer> roleIds) {
}
