package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa un departamento parametrizado en el sistema.")
public record DepartmentResponse(
        Integer id,
        Integer countryId,
        String countryCode,
        String countryName,
        String code,
        String name,
        Boolean active) {
}
