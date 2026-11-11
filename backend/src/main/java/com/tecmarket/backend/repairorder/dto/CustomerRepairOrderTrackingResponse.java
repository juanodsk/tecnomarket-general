package com.tecmarket.backend.repairorder.dto;

import java.time.LocalDateTime;

public record CustomerRepairOrderTrackingResponse(
        String orderNumber,
        String businessName,
        String branchName,
        String deviceLabel,
        String serviceName,
        String statusCode,
        String statusName,
        LocalDateTime receivedAt,
        LocalDateTime estimatedDeliveryAt,
        LocalDateTime deliveredAt) {
}
