package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record QuickRepairOrderDeviceRequest(
        @NotNull @Positive
        @Schema(example = "1")
        Integer deviceTypeId,

        @NotNull @Positive
        @Schema(example = "1")
        Integer brandId,

        @NotBlank @Size(max = 50)
        @Schema(example = "iPhone 13 Pro")
        String model,

        @Size(max = 60)
        @Schema(example = "SN-ABC-12345", nullable = true)
        String serialNumber,

        @Size(max = 60)
        @Schema(example = "351756051523999", nullable = true)
        String imei,

        @NotBlank @Size(max = 20)
        @Schema(example = "Azul")
        String color,

        @Schema(example = "2026-09-24T10:30:00", nullable = true)
        LocalDateTime registeredAt) {
}
