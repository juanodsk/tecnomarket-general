package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Alta atomica de cliente, dispositivo y orden desde recepcion.")
public record CreateQuickRepairOrderRequest(
        @Valid @NotNull QuickRepairOrderCustomerRequest customer,
        @Valid @NotNull QuickRepairOrderDeviceRequest device,
        @Valid @NotNull QuickRepairOrderDetailsRequest order) {
}
