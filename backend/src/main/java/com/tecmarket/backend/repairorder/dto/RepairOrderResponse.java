package com.tecmarket.backend.repairorder.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RepairOrderResponse(
        Integer id,
        String orderNumber,
        Integer orderSequence,
        Integer businessId,
        Integer branchId,
        Integer branchNumber,
        String branchName,
        Integer customerId,
        String customerName,
        String customerSurname,
        String customerEmail,
        Integer customerDeviceId,
        String customerDeviceLabel,
        Integer appointmentId,
        Integer businessServiceId,
        String businessServiceName,
        Integer statusId,
        String statusCode,
        String statusName,
        Integer receivedByBusinessUserId,
        String receivedByBusinessUserName,
        Integer assignedBusinessUserId,
        String assignedBusinessUserName,
        String problemDescription,
        String diagnosis,
        BigDecimal estimatedPrice,
        BigDecimal finalPrice,
        LocalDateTime receivedAt,
        LocalDateTime estimatedDeliveryAt,
        LocalDateTime deliveredAt,
        Boolean active) {
}
