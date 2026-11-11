package com.tecmarket.backend.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(
        @NotNull
        @Schema(example = "1")
        Integer branchId,

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
        @Schema(example = "1")
        Integer statusId,

        @NotNull
        @Schema(example = "2026-05-20T09:00:00")
        LocalDateTime scheduledAt,

        @NotBlank
        @Size(max = 200)
        @Schema(example = "El equipo no enciende y presenta olor a quemado.")
        String problemDescription) {
}
