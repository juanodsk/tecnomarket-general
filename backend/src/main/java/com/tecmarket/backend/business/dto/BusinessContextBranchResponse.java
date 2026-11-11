package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Sede accesible dentro del contexto del usuario autenticado.")
public record BusinessContextBranchResponse(
        @Schema(example = "1") Integer id,
        @Schema(example = "1") Integer branchNumber,
        @Schema(example = "Sede principal") String name,
        @Schema(example = "1") Integer cityId,
        @Schema(example = "Bogota") String cityName,
        @Schema(example = "Calle 10 #20-30") String address,
        @Schema(example = "true") Boolean primaryBranch) {
}
