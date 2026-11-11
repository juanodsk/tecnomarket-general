package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para rechazar una solicitud de negocio.")
public record RejectBusinessRequest(
        @NotBlank @Size(max = 1000) @Schema(example = "Documentación incompleta o inconsistente.") String rejectionReason) {
}
