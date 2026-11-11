package com.tecmarket.backend.business.dto;

import java.time.LocalTime;

public record BusinessScheduleResponse(
        Integer id,
        Integer businessId,
        Integer branchId,
        String branchName,
        Integer dayOfWeek,
        LocalTime openTime,
        LocalTime closeTime,
        Integer slotOrder,
        Boolean active) {
}
