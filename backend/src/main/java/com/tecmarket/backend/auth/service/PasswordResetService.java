package com.tecmarket.backend.auth.service;

import com.tecmarket.backend.auth.entity.PasswordResetToken;
import com.tecmarket.backend.auth.repository.PasswordResetTokenRepository;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;

@Service
public class PasswordResetService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private final PasswordResetTokenRepository tokenRepository;
    private final SecurityVersionService securityVersionService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.auth.password-reset.expiration-minutes:30}")
    private long expirationMinutes;
    @Value("${app.auth.account-invitation.expiration-hours:48}")
    private long invitationExpirationHours;
    @Value("${app.auth.password-reset.url:http://localhost:3000/restablecer-contrasena}")
    private String resetUrl;
    @Value("${app.auth.email-verification.from:no-reply@tecmarket.local}")
    private String fromEmail;
    @Value("${app.notification.resend.api-key:}")
    private String resendApiKey;
    @Value("${app.notification.resend.base-url:https://api.resend.com}")
    private String resendBaseUrl;

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepository,
            SecurityVersionService securityVersionService,
            UserService userService,
            PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.securityVersionService = securityVersionService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void request(String email) {
        userService.findByEmail(email.trim().toLowerCase())
                .filter(user -> Boolean.TRUE.equals(user.getActive()))
                .ifPresent(user -> createAndSend(user, false, null));
    }

    @Transactional
    public void inviteNewAccount(AppUser user, String orderNumber) {
        if (!Boolean.TRUE.equals(user.getActive()) || user.getDeletedAt() != null) {
            throw new ConflictException("quick_order.customer.inactive");
        }
        createAndSend(user, true, orderNumber);
    }

    @Transactional
    public void reset(String rawToken, String newPassword) {
        PasswordResetToken token = tokenRepository.findByTokenHash(hash(normalize(rawToken)))
                .orElseThrow(() -> new ResourceNotFoundException("auth.password_reset.token.invalid"));
        LocalDateTime now = LocalDateTime.now();
        if (!Boolean.TRUE.equals(token.getActive()) || token.getUsedAt() != null) {
            throw new ConflictException("auth.password_reset.token.used");
        }
        if (!token.getExpiresAt().isAfter(now)) {
            token.setActive(false);
            token.setDeletedAt(now);
            throw new ConflictException("auth.password_reset.token.expired");
        }

        AppUser user = token.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            user.setEmailVerified(true);
            user.setEmailVerifiedAt(now);
        }
        token.setActive(false);
        token.setUsedAt(now);
        token.setDeletedAt(now);
        deactivateTokens(user.getId(), now);
        securityVersionService.invalidateUser(user);
    }

    private void createAndSend(AppUser user, boolean invitation, String orderNumber) {
        LocalDateTime now = LocalDateTime.now();
        deactivateTokens(user.getId(), now);
        String rawToken = generateToken();
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setTokenHash(hash(rawToken));
        long tokenExpirationMinutes = invitation
                ? Math.max(1, invitationExpirationHours) * 60
                : Math.max(5, expirationMinutes);
        token.setExpiresAt(now.plusMinutes(tokenExpirationMinutes));
        tokenRepository.save(token);
        sendEmail(user, rawToken, invitation, orderNumber, tokenExpirationMinutes);
    }

    private void deactivateTokens(Integer userId, LocalDateTime now) {
        tokenRepository.findByUserIdAndActiveTrue(userId).forEach(token -> {
            token.setActive(false);
            token.setDeletedAt(now);
        });
    }

    private void sendEmail(
            AppUser user,
            String rawToken,
            boolean invitation,
            String orderNumber,
            long tokenExpirationMinutes) {
        if (!StringUtils.hasText(resendApiKey)) {
            throw new ConflictException("auth.password_reset.email.send_failed");
        }
        String link = resetUrl.replaceAll("/+$", "") + "#token="
                + URLEncoder.encode(rawToken, StandardCharsets.UTF_8);
        String subject = invitation
                ? "Crea la contraseña de tu cuenta TecMarket"
                : "Restablece tu contraseña de TecMarket";
        String text = invitation
                ? "Hola %s. Se ha creado satisfactoriamente tu cuenta de TecMarket al registrar la orden %s. "
                        .formatted(user.getName(), orderNumber)
                        + "Crea tu contraseña usando este enlace: " + link
                        + ". El enlace vence en " + formatExpiration(tokenExpirationMinutes) + "."
                : "Usa este enlace para restablecer tu contraseña: " + link;
        Map<String, Object> payload = Map.of(
                "from", fromEmail,
                "to", user.getEmail(),
                "subject", subject,
                "text", text);
        try {
            RestClient.builder().baseUrl(resendBaseUrl).build()
                    .post().uri("/emails")
                    .header("Authorization", "Bearer " + resendApiKey.trim())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload).retrieve().toBodilessEntity();
        } catch (RestClientException exception) {
            throw new ConflictException("auth.password_reset.email.send_failed");
        }
    }

    private String formatExpiration(long minutes) {
        if (minutes % 60 == 0) {
            return (minutes / 60) + " horas";
        }
        return minutes + " minutos";
    }

    private String normalize(String rawToken) {
        if (!StringUtils.hasText(rawToken)) {
            throw new ResourceNotFoundException("auth.password_reset.token.invalid");
        }
        return rawToken.trim();
    }

    private String generateToken() {
        byte[] bytes = new byte[64];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available.", exception);
        }
    }
}
