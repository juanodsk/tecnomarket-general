package com.tecmarket.backend.auth.dto;

import java.util.List;

public record AuthUserPermissionsResponse(
        Integer userId,
        List<String> roles,
        List<String> permissions) {
}
