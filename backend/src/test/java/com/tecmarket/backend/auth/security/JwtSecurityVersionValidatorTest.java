package com.tecmarket.backend.auth.security;

import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import com.tecmarket.backend.auth.repository.RefreshTokenRepository;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtSecurityVersionValidatorTest {

    private final AppUserRepository appUserRepository = mock(AppUserRepository.class);
    private final RefreshTokenRepository refreshTokenRepository = mock(RefreshTokenRepository.class);
    private final JwtSecurityVersionValidator validator = new JwtSecurityVersionValidator(
            appUserRepository,
            refreshTokenRepository);
    private final UUID sessionId = UUID.randomUUID();

    @Test
    void acceptsTokenWhenUserIsActiveAndVersionMatches() {
        AppUser user = activeUser(7);
        when(appUserRepository.findById(12)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.existsByFamilyIdAndUserIdAndRevokedFalseAndExpiresAtAfter(
                org.mockito.ArgumentMatchers.eq(sessionId),
                org.mockito.ArgumentMatchers.eq(12),
                org.mockito.ArgumentMatchers.any(LocalDateTime.class))).thenReturn(true);

        OAuth2TokenValidatorResult result = validator.validate(jwt("12", 7));

        assertThat(result.hasErrors()).isFalse();
    }

    @Test
    void rejectsTokenAfterSecurityVersionChanges() {
        AppUser user = activeUser(8);
        when(appUserRepository.findById(12)).thenReturn(Optional.of(user));

        OAuth2TokenValidatorResult result = validator.validate(jwt("12", 7));

        assertThat(result.hasErrors()).isTrue();
    }

    @Test
    void rejectsTokenWhenUserIsDeactivated() {
        AppUser user = activeUser(7);
        user.setActive(false);
        when(appUserRepository.findById(12)).thenReturn(Optional.of(user));

        assertThat(validator.validate(jwt("12", 7)).hasErrors()).isTrue();
    }

    @Test
    void rejectsLegacyTokenWithoutSecurityVersion() {
        Jwt jwt = new Jwt("token", Instant.now(), Instant.now().plusSeconds(60),
                Map.of("alg", "HS256"), Map.of("sub", "12"));

        assertThat(validator.validate(jwt).hasErrors()).isTrue();
    }

    @Test
    void rejectsAccessTokenAfterItsSessionFamilyIsRevoked() {
        AppUser user = activeUser(7);
        when(appUserRepository.findById(12)).thenReturn(Optional.of(user));

        assertThat(validator.validate(jwt("12", 7)).hasErrors()).isTrue();
    }

    private AppUser activeUser(int securityVersion) {
        AppUser user = new AppUser();
        user.setId(12);
        user.setActive(true);
        user.setSecurityVersion(securityVersion);
        return user;
    }

    private Jwt jwt(String subject, int securityVersion) {
        return new Jwt("token", Instant.now(), Instant.now().plusSeconds(60),
                Map.of("alg", "HS256"),
                Map.of(
                        "sub", subject,
                        "security_version", securityVersion,
                        "sid", sessionId.toString()));
    }
}
