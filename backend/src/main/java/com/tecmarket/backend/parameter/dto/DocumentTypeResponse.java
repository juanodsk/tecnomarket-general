package com.tecmarket.backend.parameter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa un tipo de documento parametrizado en el sistema.")
public record DocumentTypeResponse(
        Integer id,
        String code,
        String name,
        String description,
        Boolean appliesToPerson,
        Boolean appliesToCompany,
        Boolean active) {
}
