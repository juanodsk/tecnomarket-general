package com.tecmarket.backend.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateAppointmentRequest(
        @NotNull
        @Schema(example = "5")
        Integer customerId,

        @NotNull
        @Schema(example = "2")
        Integer customerDeviceId,

        @NotNull
        @Schema(example = "1")
        Integer businessServiceId,

        @NotNull
        @Schema(example = "2")
        Integer statusId,

        @NotNull
        @Schema(example = "2026-05-20T10:30:00")
        LocalDateTime scheduledAt,

        @NotBlank
        @Size(max = 200)
        @Schema(example = "No enciende, posible daño en tarjeta lógica.")
        String problemDescription,

        @Schema(example = "true")
        Boolean active) {
}
