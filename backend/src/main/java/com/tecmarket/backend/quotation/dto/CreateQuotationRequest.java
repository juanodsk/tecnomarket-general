package com.tecmarket.backend.quotation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record CreateQuotationRequest(
        @NotNull
        @Schema(example = "1")
        Integer repairOrderId,

        @Size(max = 500)
        @Schema(example = "Cotización válida por 5 días calendario.")
        String notes,

        @Schema(example = "2026-05-21T23:59:00")
        LocalDateTime expiresAt,

        @NotEmpty
        @Size(max = 100)
        @Valid
        List<CreateQuotationItemRequest> items) {
}
