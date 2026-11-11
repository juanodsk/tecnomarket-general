package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record QuickRepairOrderDetailsRequest(
        @NotNull @Positive
        @Schema(example = "1")
        Integer branchId,

        @NotNull @Positive
        @Schema(example = "1")
        Integer businessServiceId,

        @NotNull @Positive
        @Schema(example = "3")
        Integer assignedBusinessUserId,

        @NotBlank @Size(max = 2000)
        @Schema(example = "No enciende y no responde al cargar.")
        String problemDescription,

        @Size(max = 2000)
        @Schema(example = "Posible falla en power IC.", nullable = true)
        String diagnosis,

        @DecimalMin("0.00")
        @Schema(example = "150000.00", nullable = true)
        BigDecimal estimatedPrice,

        @Schema(example = "2026-09-27T18:00:00", nullable = true)
        LocalDateTime estimatedDeliveryAt,

        @Schema(example = "2026-09-24T10:30:00", nullable = true)
        LocalDateTime receivedAt) {
}
