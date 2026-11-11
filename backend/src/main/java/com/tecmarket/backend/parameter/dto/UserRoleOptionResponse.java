package com.tecmarket.backend.parameter.dto;

public record UserRoleOptionResponse(
        Integer roleId,
        String code,
        String name,
        String description,
        String scope,
        Boolean assigned) {
}
