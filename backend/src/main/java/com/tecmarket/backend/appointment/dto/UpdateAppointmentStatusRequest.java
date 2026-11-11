package com.tecmarket.backend.appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateAppointmentStatusRequest(
        @NotNull
        @Schema(example = "2")
        Integer statusId) {
}
