package com.tecmarket.backend.appointment.dto;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Integer id,
        Integer businessId,
        Integer branchId,
        String branchName,
        Integer customerId,
        String customerName,
        String customerSurname,
        String customerEmail,
        Integer customerDeviceId,
        String customerDeviceLabel,
        Integer businessServiceId,
        String businessServiceName,
        Integer statusId,
        String statusCode,
        String statusName,
        LocalDateTime scheduledAt,
        String problemDescription,
        Boolean active) {
}
