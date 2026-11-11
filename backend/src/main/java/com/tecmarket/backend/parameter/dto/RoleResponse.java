package com.tecmarket.backend.parameter.dto;

public record RoleResponse(
        Integer id,
        String code,
        String name,
        String description,
        String scope,
        Boolean active) {
}
