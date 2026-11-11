package com.tecmarket.backend.parameter.dto;

public record PermissionResponse(
        Integer id,
        Integer moduleId,
        String moduleCode,
        String moduleName,
        String code,
        String name,
        String description,
        Boolean active) {
}
