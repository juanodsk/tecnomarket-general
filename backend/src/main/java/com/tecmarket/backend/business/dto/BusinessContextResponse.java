package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Contexto efectivo del usuario autenticado dentro de un negocio.")
public record BusinessContextResponse(
        @Schema(example = "12") Integer userId,
        @Schema(example = "1") Integer businessId,
        @Schema(example = "TecMarket Centro") String businessName,
        String businessLogo,
        @Schema(description = "Indica si el acceso proviene del rol global PLATFORM_ADMIN.", example = "false")
        Boolean platformAdmin,
        @Schema(description = "ID de business_user. Es null para PLATFORM_ADMIN sin membresia empresarial.", example = "8")
        Integer businessUserId,
        @Schema(description = "ID del rol empresarial. Es null para PLATFORM_ADMIN sin membresia empresarial.", example = "4")
        Integer roleId,
        @Schema(description = "Codigo del rol empresarial. Es null para PLATFORM_ADMIN sin membresia empresarial.", example = "TECHNICIAN")
        String roleCode,
        @Schema(description = "Nombre del rol empresarial.", example = "Tecnico")
        String roleName,
        List<String> permissions,
        List<BusinessContextBranchResponse> branches) {
}
