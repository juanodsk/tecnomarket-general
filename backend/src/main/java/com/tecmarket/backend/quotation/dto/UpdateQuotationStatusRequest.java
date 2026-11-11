package com.tecmarket.backend.quotation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateQuotationStatusRequest(
        @NotNull
        @Schema(example = "3")
        Integer statusId,

        @Size(max = 500)
        @Schema(example = "Cliente aprobó por WhatsApp.")
        String notes) {
}
