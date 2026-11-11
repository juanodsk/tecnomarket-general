package com.tecmarket.backend.auth.service;

import com.tecmarket.backend.auth.entity.RefreshToken;
import com.tecmarket.backend.auth.repository.RefreshTokenRepository;
import com.tecmarket.backend.user.entity.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private RefreshTokenService service;
    private AppUser user;

    @BeforeEach
    void setUp() {
        service = new RefreshTokenService(refreshTokenRepository, 7, 5, 30);
        user = new AppUser();
        user.setId(10);
        user.setActive(true);
        user.setEmailVerified(true);
        user.setSecurityVersion(0);
    }

    @Test
    void issueStoresOnlyHash() {
        String rawToken = service.issue(user);

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());
        assertThat(rawToken).hasSizeGreaterThan(64);
        assertThat(captor.getValue().getTokenHash())
                .hasSize(64)
                .isNotEqualTo(rawToken);
        assertThat(captor.getValue().getIssuedSecurityVersion()).isZero();
    }

    @Test
    void rotatePreservesFamilyAndAbsoluteExpiration() {
        UUID familyId = UUID.randomUUID();
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(3);
        RefreshToken current = token(familyId, expiresAt, false);
        when(refreshTokenRepository.findForUpdateByTokenHash(any())).thenReturn(Optional.of(current));

        RefreshTokenService.RefreshSession result = service.rotate("valid-refresh-token");

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());
        RefreshToken replacement = captor.getValue();
        assertThat(result.user()).isSameAs(user);
        assertThat(replacement.getFamilyId()).isEqualTo(familyId);
        assertThat(replacement.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(current.getRevoked()).isTrue();
        assertThat(current.getReplacedByTokenHash()).isEqualTo(replacement.getTokenHash());
    }

    @Test
    void expiredTokenRevokesItsFamily() {
        RefreshToken expired = token(UUID.randomUUID(), LocalDateTime.now().minusMinutes(1), false);
        when(refreshTokenRepository.findForUpdateByTokenHash(any())).thenReturn(Optional.of(expired));

        assertThatThrownBy(() -> service.rotate("expired-refresh-token"))
                .isInstanceOf(BadCredentialsException.class);

        assertThat(expired.getRevoked()).isTrue();
        verify(refreshTokenRepository).revokeActiveFamily(expired.getFamilyId(), expired.getRevokedAt());
        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    void logoutRejectsTokenOwnedByAnotherUser() {
        RefreshToken token = token(UUID.randomUUID(), LocalDateTime.now().plusDays(1), false);
        when(refreshTokenRepository.findForUpdateByTokenHash(any())).thenReturn(Optional.of(token));

        assertThatThrownBy(() -> service.revoke("foreign-refresh-token", 99))
                .isInstanceOf(BadCredentialsException.class);

        verify(refreshTokenRepository, never()).revokeActiveFamily(any(), any());
    }

    @Test
    void rotateRejectsTokenIssuedBeforeSecurityVersionChanged() {
        user.setSecurityVersion(2);
        RefreshToken stale = token(UUID.randomUUID(), LocalDateTime.now().plusDays(1), false);
        stale.setIssuedSecurityVersion(1);
        when(refreshTokenRepository.findForUpdateByTokenHash(any())).thenReturn(Optional.of(stale));

        assertThatThrownBy(() -> service.rotate("stale-refresh-token"))
                .isInstanceOf(BadCredentialsException.class);

        verify(refreshTokenRepository).revokeActiveFamily(stale.getFamilyId(), stale.getRevokedAt());
        verify(refreshTokenRepository, never()).save(any());
    }

    private RefreshToken token(UUID familyId, LocalDateTime expiresAt, boolean revoked) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setTokenHash("a".repeat(64));
        token.setFamilyId(familyId);
        token.setExpiresAt(expiresAt);
        token.setRevoked(revoked);
        token.setIssuedSecurityVersion(user.getSecurityVersion());
        return token;
    }
}
