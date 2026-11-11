package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Datos para crear una sede o local del negocio.")
public record CreateBusinessBranchRequest(
        @NotBlank @Size(max = 120) @Schema(example = "Sede Norte") String name,
        @NotNull @Schema(example = "1") Integer cityId,
        @NotBlank @Size(max = 200) @Schema(example = "Carrera 15 # 100-20") String address,
        @Size(max = 120) @Schema(example = "Usaquen") String neighborhood,
        @Size(max = 30) @Schema(example = "+573001112233") String phone,
        @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") @Schema(example = "4.6845000") BigDecimal latitude,
        @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") @Schema(example = "-74.0478000") BigDecimal longitude,
        @DecimalMin(value = "0.1") @Schema(example = "8.0") BigDecimal serviceRadiusKm,
        @Schema(example = "true") Boolean primaryBranch) {
}
