package com.tecmarket.backend.auth.service;

import com.tecmarket.backend.auth.repository.RefreshTokenRepository;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SecurityVersionServiceTest {

    @Test
    void invalidationBumpsVersionAndRevokesEveryRefreshToken() {
        AppUserRepository users = mock(AppUserRepository.class);
        RefreshTokenRepository refreshTokens = mock(RefreshTokenRepository.class);
        SecurityVersionService service = new SecurityVersionService(users, refreshTokens);
        AppUser user = new AppUser();
        user.setId(42);
        user.setSecurityVersion(3);
        when(users.findById(42)).thenReturn(Optional.of(user));

        service.invalidateUser(42);

        assertThat(user.getSecurityVersion()).isEqualTo(4);
        verify(refreshTokens).revokeAllByUserId(eq(42), any(LocalDateTime.class));
    }
}
