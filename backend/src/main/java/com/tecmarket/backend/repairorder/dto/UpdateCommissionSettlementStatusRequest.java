package com.tecmarket.backend.repairorder.dto;

import com.tecmarket.backend.repairorder.entity.CommissionSettlementStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateCommissionSettlementStatusRequest(
        @NotNull
        @Schema(description = "Nuevo estado de la liquidacion.", example = "APPROVED")
        CommissionSettlementStatus status) {
}
