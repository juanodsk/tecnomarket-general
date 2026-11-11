package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Respuesta de una solicitud de negocio.")
public record BusinessRequestResponse(
        Integer id,
        String documentTypeCode,
        String documentNumber,
        String businessName,
        String legalName,
        String email,
        String phone,
        Integer cityId,
        String cityName,
        String address,
        String legalFile,
        String status,
        Integer requestedByUserId,
        String requestedByUserName,
        Integer reviewedByUserId,
        String reviewedByUserName,
        LocalDateTime reviewedAt,
        String rejectionReason,
        LocalDateTime createdAt) {
}
