package com.tecmarket.backend.repairorder.dto;

import com.tecmarket.backend.repairorder.entity.RepairOrderHistoryVisibility;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateRepairOrderStatusRequest(
        @NotNull
        @Schema(example = "2")
        Integer statusId,

        @Size(max = 2000)
        @Schema(example = "Se finalizó diagnóstico y pasa a reparación.")
        String comment,

        @Schema(example = "INTERNAL")
        RepairOrderHistoryVisibility visibility) {
}
