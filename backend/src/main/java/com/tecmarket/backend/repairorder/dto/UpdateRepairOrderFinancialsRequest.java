package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Valor total final cobrado por una orden de reparacion.")
public record UpdateRepairOrderFinancialsRequest(
        @NotNull
        @DecimalMin("0.00")
        @Digits(integer = 10, fraction = 2)
        @Schema(description = "Costo total del servicio incluyendo repuestos.", example = "300000.00")
        BigDecimal finalPrice) {
}
