package com.tecmarket.backend.sale.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateSaleRequest(
        @NotNull
        @Schema(example = "1")
        Integer branchId,

        @Schema(example = "2")
        Integer customerId,

        @NotNull
        @Schema(example = "1")
        Integer paymentMethodId,

        @NotEmpty
        @Size(max = 100)
        @Valid
        List<CreateSaleItemRequest> items) {
}
