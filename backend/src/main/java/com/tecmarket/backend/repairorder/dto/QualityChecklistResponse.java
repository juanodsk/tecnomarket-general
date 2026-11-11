package com.tecmarket.backend.repairorder.dto;

import java.time.LocalDateTime;
import java.util.List;

public record QualityChecklistResponse(
        Integer businessId,
        String intakeTicketCode,
        Integer repairOrderId,
        Integer checkedByUserId,
        String checkedByUserName,
        LocalDateTime checkedAt,
        List<QualityChecklistItemResponse> items) {
}
