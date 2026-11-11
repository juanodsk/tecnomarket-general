package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record RepairOrderAttachmentResponse(
        @Schema(example = "1")
        Integer id,

        @Schema(example = "10")
        Integer repairOrderId,

        @Schema(example = "5")
        Integer uploadedByBusinessUserId,

        @Schema(example = "juan.perez@empresa.com")
        String uploadedByEmail,

        @Schema(example = "Juan Perez")
        String uploadedByName,

        @Schema(example = "https://cdn.tudominio.com/repair-orders/OS-001-000101/pantalla-frontal.jpg")
        String fileUrl,

        @Schema(example = "pantalla-frontal.jpg")
        String originalFileName,

        @Schema(example = "image/jpeg")
        String contentType,

        @Schema(example = "248120")
        Long fileSize,

        @Schema(example = "f6b0c1d2e3f4567890abcdef1234567890abcdef1234567890abcdef12345678")
        String checksumSha256,

        @Schema(example = "Foto frontal al recibir el equipo.")
        String description,

        @Schema(example = "2026-05-16T10:30:00")
        LocalDateTime createdAt) {
}
