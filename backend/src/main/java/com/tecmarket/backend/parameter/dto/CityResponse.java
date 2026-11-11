package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa una ciudad parametrizada en el sistema.")
public record CityResponse(
        Integer id,
        Integer countryId,
        String countryCode,
        String countryName,
        Integer departmentId,
        String departmentCode,
        String departmentName,
        String name,
        Boolean active) {
}
