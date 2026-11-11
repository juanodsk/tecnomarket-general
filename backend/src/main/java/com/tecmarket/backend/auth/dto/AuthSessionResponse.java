package com.tecmarket.backend.auth.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuthSessionResponse(
        UUID sessionId,
        String clientDescription,
        String ipAddress,
        LocalDateTime createdAt,
        LocalDateTime lastUsedAt,
        LocalDateTime expiresAt) {
}
