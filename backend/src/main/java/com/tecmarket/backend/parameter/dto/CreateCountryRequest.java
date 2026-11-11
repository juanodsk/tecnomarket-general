package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para crear un pais.")
public record CreateCountryRequest(
        @NotBlank @Size(max = 10) @Schema(description = "Codigo unico del pais. Se almacena en mayusculas.", example = "CO") String code,

        @NotBlank @Size(max = 100) @Schema(description = "Nombre del pais.", example = "Colombia") String name) {
}
