package com.tecmarket.backend.parameter.dto;

public record RoleAssignedPermissionResponse(
        Integer permissionId,
        String code,
        String name,
        String description,
        String scope,
        Integer moduleId,
        String moduleCode,
        String moduleName) {
}
