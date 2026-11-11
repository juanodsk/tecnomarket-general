package com.tecmarket.backend.auth.security;

import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import com.tecmarket.backend.auth.repository.RefreshTokenRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtSecurityVersionValidator implements OAuth2TokenValidator<Jwt> {

    private static final OAuth2Error INVALID_TOKEN = new OAuth2Error(
            "invalid_token",
            "The access token is no longer valid.",
            null);

    private final AppUserRepository appUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtSecurityVersionValidator(
            AppUserRepository appUserRepository,
            RefreshTokenRepository refreshTokenRepository) {
        this.appUserRepository = appUserRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        Integer userId = parseUserId(jwt.getSubject());
        Integer tokenSecurityVersion = parseSecurityVersion(jwt.getClaim("security_version"));
        UUID sessionId = parseSessionId(jwt.getClaimAsString("sid"));
        if (userId == null || tokenSecurityVersion == null || sessionId == null) {
            return OAuth2TokenValidatorResult.failure(INVALID_TOKEN);
        }

        try {
            return appUserRepository.findById(userId)
                    .filter(this::isActive)
                    .filter(user -> currentSecurityVersion(user).equals(tokenSecurityVersion))
                    .filter(user -> refreshTokenRepository
                            .existsByFamilyIdAndUserIdAndRevokedFalseAndExpiresAtAfter(
                                    sessionId,
                                    user.getId(),
                                    LocalDateTime.now()))
                    .map(ignored -> OAuth2TokenValidatorResult.success())
                    .orElseGet(() -> OAuth2TokenValidatorResult.failure(INVALID_TOKEN));
        } catch (RuntimeException exception) {
            return OAuth2TokenValidatorResult.failure(INVALID_TOKEN);
        }
    }

    private Integer parseUserId(String subject) {
        try {
            return Integer.valueOf(subject);
        } catch (NumberFormatException | NullPointerException exception) {
            return null;
        }
    }

    private Integer parseSecurityVersion(Object claim) {
        if (!(claim instanceof Number number)) {
            return null;
        }
        long value = number.longValue();
        return value >= 0 && value <= Integer.MAX_VALUE ? (int) value : null;
    }

    private UUID parseSessionId(String claim) {
        try {
            return UUID.fromString(claim);
        } catch (IllegalArgumentException | NullPointerException exception) {
            return null;
        }
    }

    private boolean isActive(AppUser user) {
        return Boolean.TRUE.equals(user.getActive()) && user.getDeletedAt() == null;
    }

    private Integer currentSecurityVersion(AppUser user) {
        return user.getSecurityVersion() == null ? 0 : user.getSecurityVersion();
    }
}
