package com.tecmarket.backend.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Sede o local de un negocio.")
public record BusinessBranchResponse(
        @Schema(example = "1") Integer id,
        @Schema(example = "1") Integer businessId,
        @Schema(example = "TecMarket Centro") String businessName,
        @Schema(example = "1") Integer branchNumber,
        @Schema(example = "Sede Norte") String name,
        @Schema(example = "1") Integer countryId,
        @Schema(example = "Colombia") String countryName,
        @Schema(example = "1") Integer cityId,
        @Schema(example = "Bogota") String cityName,
        @Schema(example = "Carrera 15 # 100-20") String address,
        @Schema(example = "Usaquen") String neighborhood,
        @Schema(example = "+573001112233") String phone,
        @Schema(example = "4.6845000") BigDecimal latitude,
        @Schema(example = "-74.0478000") BigDecimal longitude,
        @Schema(example = "8.00") BigDecimal serviceRadiusKm,
        @Schema(example = "true") Boolean primaryBranch,
        @Schema(example = "true") Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
