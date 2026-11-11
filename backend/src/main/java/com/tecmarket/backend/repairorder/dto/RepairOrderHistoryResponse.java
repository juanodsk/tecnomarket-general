package com.tecmarket.backend.repairorder.dto;

import com.tecmarket.backend.repairorder.entity.RepairOrderHistoryVisibility;

import java.time.LocalDateTime;

public record RepairOrderHistoryResponse(
        Integer id,
        Integer repairOrderId,
        Integer statusId,
        String statusCode,
        String statusName,
        Integer createdByUserId,
        String createdByUserName,
        String createdByUserEmail,
        String comment,
        RepairOrderHistoryVisibility visibility,
        LocalDateTime createdAt) {
}
