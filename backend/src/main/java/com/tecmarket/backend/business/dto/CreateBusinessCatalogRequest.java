package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear un negocio desde el catálogo administrativo.")
public record CreateBusinessCatalogRequest(
        @NotBlank @Size(max = 15) @Schema(description = "Código del tipo de documento legal del negocio.", example = "NIT") String documentTypeCode,

        @NotBlank @Size(max = 50) @Schema(description = "Número de documento legal del negocio.", example = "901234567-1") String documentNumber,

        @NotBlank @Size(max = 150) @Schema(description = "Nombre comercial del negocio.", example = "Tech Store Norte") String name,

        @NotBlank @Size(max = 200) @Schema(description = "Razón social del negocio.", example = "TECH STORE NORTE S.A.S") String legalName,

        @NotBlank @Email @Size(max = 150) @Schema(description = "Correo principal del negocio.", example = "contacto@techstore.com") String email,

        @NotBlank @Size(max = 30) @Schema(description = "Teléfono principal del negocio.", example = "+573001112233") String phone,

        @NotBlank @Size(max = 200) @Schema(description = "Dirección principal del negocio.", example = "Calle 123 #45-67") String address,

        @NotBlank @Size(max = 255) @Schema(description = "Descripción corta del negocio.", example = "Centro de servicio técnico especializado en celulares y portátiles.") String description,

        @NotNull @Schema(description = "ID de la ciudad donde opera el negocio.", example = "1") Integer cityId,

        @Size(max = 500) @Schema(description = "Debe ser null. El logo se carga después por el endpoint seguro de imagen.", nullable = true) String logo,

        @Size(max = 500) @Schema(description = "Debe ser null. La portada se carga después por el endpoint seguro de imagen.", nullable = true) String coverImage) {
}
