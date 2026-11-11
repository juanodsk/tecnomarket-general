package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateCustomerDeviceRequest(
        @NotNull
        @Schema(example = "5")
        Integer customerId,

        @NotNull
        @Schema(example = "1")
        Integer deviceTypeId,

        @NotNull
        @Schema(example = "1")
        Integer brandId,

        @NotBlank
        @Size(max = 50)
        @Schema(example = "iPhone 13 Pro")
        String model,

        @Size(max = 60)
        @Schema(example = "SN-ABC-12345")
        String serialNumber,

        @Size(max = 60)
        @Schema(example = "351756051523999")
        String imei,

        @NotNull
        @Schema(example = "1")
        Integer registeredByBusinessUserId,

        @NotBlank
        @Size(max = 20)
        @Schema(example = "Azul")
        String color,

        @Schema(example = "2026-05-15T14:30:00")
        LocalDateTime registeredAt) {
}
