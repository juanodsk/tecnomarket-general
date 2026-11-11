package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa un pais parametrizado en el sistema.")
public record CountryResponse(
        Integer id,
        String code,
        String name,
        Boolean active) {
}
