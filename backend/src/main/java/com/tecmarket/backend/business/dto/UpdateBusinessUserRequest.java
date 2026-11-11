package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Datos para actualizar una asignación usuario-rol de negocio.")
public record UpdateBusinessUserRequest(
        @NotNull @Schema(example = "4") Integer roleId,
        @NotNull @Size(min = 1, max = 50)
        @Schema(example = "[1, 2]", description = "Sedes operativas asignadas al empleado.")
        List<@NotNull @Positive Integer> branchIds,
        @Schema(example = "true") Boolean active,
        @DecimalMin("0.00") @DecimalMax("100.00") @Digits(integer = 3, fraction = 2)
        @Schema(description = "Porcentaje de comision por reparacion. Aplica para tecnicos.", example = "20.00")
        BigDecimal commissionPercentage) {
}
