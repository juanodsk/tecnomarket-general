package com.tecmarket.backend.auth.service;

import com.tecmarket.backend.auth.entity.PasswordResetToken;
import com.tecmarket.backend.auth.repository.PasswordResetTokenRepository;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.service.UserService;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PasswordResetServiceTest {

    @Test
    void invitationStoresOnlyHashAndSendsOneTimePasswordLink() throws Exception {
        AtomicReference<String> emailPayload = new AtomicReference<>();
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/emails", exchange -> {
            emailPayload.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            byte[] response = "{\"id\":\"email_test_ok\"}".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream output = exchange.getResponseBody()) {
                output.write(response);
            }
        });
        server.start();

        try {
            PasswordResetTokenRepository tokens = mock(PasswordResetTokenRepository.class);
            SecurityVersionService securityVersions = mock(SecurityVersionService.class);
            UserService users = mock(UserService.class);
            PasswordEncoder encoder = mock(PasswordEncoder.class);
            PasswordResetService service = new PasswordResetService(tokens, securityVersions, users, encoder);
            ReflectionTestUtils.setField(service, "invitationExpirationHours", 48L);
            ReflectionTestUtils.setField(service, "resetUrl", "https://app.example.com/restablecer-contrasena");
            ReflectionTestUtils.setField(service, "fromEmail", "no-reply@example.com");
            ReflectionTestUtils.setField(service, "resendApiKey", "test-api-key");
            ReflectionTestUtils.setField(
                    service,
                    "resendBaseUrl",
                    "http://localhost:" + server.getAddress().getPort());

            AppUser user = new AppUser();
            user.setId(7);
            user.setName("Laura");
            user.setEmail("laura@example.com");
            user.setActive(true);
            when(tokens.findByUserIdAndActiveTrue(7)).thenReturn(List.of());

            service.inviteNewAccount(user, "OS-010-01-000001");

            ArgumentCaptor<PasswordResetToken> tokenCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);
            verify(tokens).save(tokenCaptor.capture());
            PasswordResetToken storedToken = tokenCaptor.getValue();
            assertThat(storedToken.getTokenHash()).hasSize(64);
            assertThat(storedToken.getExpiresAt()).isAfter(LocalDateTime.now().plusHours(47));
            assertThat(emailPayload.get())
                    .contains("Crea la contraseña de tu cuenta TecMarket")
                    .contains("OS-010-01-000001")
                    .contains("https://app.example.com/restablecer-contrasena#token=")
                    .doesNotContain(storedToken.getTokenHash());
        } finally {
            server.stop(0);
        }
    }

    @Test
    void resetChangesPasswordAndInvalidatesAllSecurityVersions() {
        PasswordResetTokenRepository tokens = mock(PasswordResetTokenRepository.class);
        SecurityVersionService securityVersions = mock(SecurityVersionService.class);
        UserService users = mock(UserService.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        PasswordResetService service = new PasswordResetService(tokens, securityVersions, users, encoder);
        AppUser user = new AppUser();
        user.setId(7);
        user.setEmailVerified(false);
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setActive(true);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        when(tokens.findByTokenHash(anyString())).thenReturn(Optional.of(token));
        when(tokens.findByUserIdAndActiveTrue(7)).thenReturn(List.of(token));
        when(encoder.encode("NewPassword456!")).thenReturn("encoded-password");

        service.reset("a".repeat(64), "NewPassword456!");

        assertThat(user.getPasswordHash()).isEqualTo("encoded-password");
        assertThat(user.getEmailVerified()).isTrue();
        assertThat(user.getEmailVerifiedAt()).isNotNull();
        assertThat(token.getUsedAt()).isNotNull();
        verify(securityVersions).invalidateUser(user);
    }
}
