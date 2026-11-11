package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record UpdateBusinessScheduleRequest(
        @NotNull @Min(1) @Max(7) @Schema(example = "1", description = "1=Lunes, 7=Domingo") Integer dayOfWeek,

        @NotNull @Schema(example = "14:00:00") LocalTime openTime,

        @NotNull @Schema(example = "18:00:00") LocalTime closeTime,

        @Schema(example = "2") Integer slotOrder,

        @Schema(example = "true") Boolean active) {
}
