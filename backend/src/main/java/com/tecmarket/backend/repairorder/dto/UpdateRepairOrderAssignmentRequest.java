package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateRepairOrderAssignmentRequest(
        @NotNull
        @Schema(example = "3")
        Integer assignedBusinessUserId) {
}
