package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para actualizar un negocio existente.")
public record UpdateBusinessCatalogRequest(
        @NotBlank @Size(max = 150) @Schema(description = "Nombre comercial del negocio.", example = "Tech Store Norte Premium") String name,

        @NotBlank @Size(max = 200) @Schema(description = "Razón social del negocio.", example = "TECH STORE NORTE PREMIUM S.A.S") String legalName,

        @NotBlank @Email @Size(max = 150) @Schema(description = "Correo principal del negocio.", example = "soporte@techstore.com") String email,

        @NotBlank @Size(max = 30) @Schema(description = "Teléfono principal del negocio.", example = "+573104445566") String phone,

        @NotBlank @Size(max = 200) @Schema(description = "Dirección principal del negocio.", example = "Avenida 10 #20-30") String address,

        @NotBlank @Size(max = 255) @Schema(description = "Descripción corta del negocio.", example = "Servicio técnico especializado y venta de accesorios.") String description,

        @NotNull @Schema(description = "ID de la ciudad donde opera el negocio.", example = "2") Integer cityId,

        @Size(max = 500) @Schema(description = "Solo admite null para retirar el logo o la URL actual sin cambios. Para reemplazarlo usa el endpoint de carga.", nullable = true) String logo,

        @Size(max = 500) @Schema(description = "Solo admite null para retirar la portada o la URL actual sin cambios. Para reemplazarla usa el endpoint de carga.", nullable = true) String coverImage,

        @Schema(description = "Estado activo del negocio.", example = "true") Boolean active) {
}
