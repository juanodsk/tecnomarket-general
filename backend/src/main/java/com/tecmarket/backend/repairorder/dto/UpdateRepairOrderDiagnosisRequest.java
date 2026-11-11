package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRepairOrderDiagnosisRequest(
        @NotBlank
        @Size(max = 2000)
        @Schema(example = "Falla confirmada en el circuito de carga.")
        String diagnosis) {
}
