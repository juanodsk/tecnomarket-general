package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear una solicitud de negocio.")
public record CreateBusinessRequest(
                @NotBlank @Size(max = 15) @Schema(example = "NIT") String documentTypeCode,
                @NotBlank @Size(max = 50) @Schema(example = "901234567-1") String documentNumber,
                @NotBlank @Size(max = 150) @Schema(example = "Tech Store Norte") String businessName,
                @Size(max = 200) @Schema(example = "TECH STORE NORTE S.A.S") String legalName,
                @NotBlank @Email @Size(max = 150) @Schema(example = "contacto@techstore.com") String email,
                @NotBlank @Size(max = 50) @Schema(example = "+573001112233") String phone,
                @NotNull @Schema(example = "1") Integer cityId,
                @Size(max = 200) @Schema(example = "Calle 123 #45-67") String address,
                @NotBlank @Size(max = 500) @Schema(example = "https://mi-cdn.com/docs/camara-comercio.pdf") String legalFile) {
}
