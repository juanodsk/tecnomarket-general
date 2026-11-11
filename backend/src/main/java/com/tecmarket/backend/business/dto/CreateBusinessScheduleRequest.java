package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record CreateBusinessScheduleRequest(
        @NotNull @Schema(example = "1") Integer branchId,

        @NotNull @Min(1) @Max(7) @Schema(example = "1", description = "1=Lunes, 7=Domingo") Integer dayOfWeek,

        @NotNull @Schema(example = "08:00:00") LocalTime openTime,

        @NotNull @Schema(example = "12:00:00") LocalTime closeTime,

        @Schema(example = "1") Integer slotOrder) {
}
