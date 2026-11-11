package com.tecmarket.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RefreshTokenRequest(
        @NotBlank
        @Size(min = 80, max = 128)
        @Schema(description = "Opaque refresh token returned by login or refresh.")
        String refreshToken) {
}
