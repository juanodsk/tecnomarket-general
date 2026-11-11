package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para actualizar un pais.")
public record UpdateCountryRequest(
        @NotBlank @Size(max = 100) @Schema(description = "Nombre del pais.", example = "Colombia") String name,

        @Schema(description = "Indica si el pais esta activo.", example = "true") Boolean active) {
}
