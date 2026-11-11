package com.tecmarket.backend.parameter.dto;

public record RolePermissionOptionResponse(
        Integer permissionId,
        String code,
        String name,
        String description,
        String scope,
        Integer moduleId,
        String moduleCode,
        String moduleName,
        Boolean assigned) {
}
