package com.tecmarket.backend.parameter.dto;

public record AppModuleResponse(
        Integer id,
        String code,
        String name,
        Boolean active) {
}
