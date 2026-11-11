package com.tecmarket.backend.auth.service;

import com.tecmarket.backend.auth.entity.EmailVerificationToken;
import com.tecmarket.backend.auth.repository.EmailVerificationTokenRepository;
import com.tecmarket.backend.common.exception.ConflictException;
import com.tecmarket.backend.common.exception.ResourceNotFoundException;
import com.tecmarket.backend.user.entity.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Value("${app.auth.email-verification.expiration-hours:24}")
    private long verificationExpirationHours;

    @Value("${app.auth.email-verification.verify-url:http://localhost:8080/api/v1/auth/verify-email}")
    private String verificationUrl;

    @Value("${app.auth.email-verification.from:no-reply@tecmarket.local}")
    private String fromEmail;

    @Value("${app.notification.resend.api-key:}")
    private String resendApiKey;

    @Value("${app.notification.resend.base-url:https://api.resend.com}")
    private String resendBaseUrl;

    public EmailVerificationService(
            EmailVerificationTokenRepository emailVerificationTokenRepository) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
    }

    @Transactional
    public void createAndSendVerification(AppUser user) {
        deactivateActiveTokens(user.getId());

        String rawToken = generateToken();
        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(user);
        token.setTokenHash(hash(rawToken));
        token.setExpiresAt(LocalDateTime.now().plusHours(Math.max(1, verificationExpirationHours)));
        token.setActive(true);

        emailVerificationTokenRepository.save(token);
        sendVerificationEmail(user, rawToken);
    }

    @Transactional
    public void verifyEmailToken(String rawToken) {
        String tokenValue = normalizeToken(rawToken);

        EmailVerificationToken token = emailVerificationTokenRepository.findByTokenHash(hash(tokenValue))
                .orElseThrow(() -> new ResourceNotFoundException("auth.verify.token.invalid"));

        if (!Boolean.TRUE.equals(token.getActive()) || token.getVerifiedAt() != null) {
            throw new ConflictException("auth.verify.token.used");
        }

        LocalDateTime now = LocalDateTime.now();
        if (token.getExpiresAt().isBefore(now)) {
            token.setActive(false);
            token.setDeletedAt(now);
            throw new ConflictException("auth.verify.token.expired");
        }

        AppUser user = token.getUser();
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(now);

        token.setVerifiedAt(now);
        token.setActive(false);
        token.setDeletedAt(now);

        deactivateActiveTokens(user.getId());
    }

    private void sendVerificationEmail(AppUser user, String token) {
        if (!StringUtils.hasText(resendApiKey)) {
            throw new ConflictException("auth.verify.email.send_failed");
        }

        String verificationLink = buildVerificationLink(token);
        String textBody = """
                Hola %s,

                Gracias por registrarte en TecMarket.
                Para activar tu cuenta, verifica tu correo en este enlace:
                %s

                Este enlace vence en %d hora(s).
                """.formatted(user.getName(), verificationLink, Math.max(1, verificationExpirationHours));

        Map<String, Object> payload = Map.of(
                "from", fromEmail,
                "to", user.getEmail(),
                "subject", "Verifica tu cuenta en TecMarket",
                "text", textBody,
                "html", """
                        <p>Hola %s,</p>
                        <p>Gracias por registrarte en TecMarket.</p>
                        <p>Para activar tu cuenta, verifica tu correo en este enlace:</p>
                        <p><a href="%s">%s</a></p>
                        <p>Este enlace vence en %d hora(s).</p>
                        """.formatted(user.getName(), verificationLink, verificationLink,
                        Math.max(1, verificationExpirationHours)));

        try {
            RestClient.builder()
                    .baseUrl(resendBaseUrl)
                    .build()
                    .post()
                    .uri("/emails")
                    .header("Authorization", "Bearer " + resendApiKey.trim())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException exception) {
            throw new ConflictException("auth.verify.email.send_failed");
        }
    }

    private void deactivateActiveTokens(Integer userId) {
        LocalDateTime now = LocalDateTime.now();
        List<EmailVerificationToken> activeTokens = emailVerificationTokenRepository.findByUserIdAndActiveTrue(userId);

        for (EmailVerificationToken activeToken : activeTokens) {
            activeToken.setActive(false);
            activeToken.setDeletedAt(now);
        }
    }

    private String buildVerificationLink(String token) {
        String normalizedVerificationUrl = verificationUrl.replaceAll("/+$", "");
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        return normalizedVerificationUrl + "#token=" + encodedToken;
    }

    private String normalizeToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new ResourceNotFoundException("auth.verify.token.invalid");
        }

        return token.trim();
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "")
                + UUID.randomUUID().toString().replace("-", "");
    }

    private String hash(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available.", exception);
        }
    }
}
