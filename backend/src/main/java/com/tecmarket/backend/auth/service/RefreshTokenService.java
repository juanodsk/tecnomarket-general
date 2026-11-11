package com.tecmarket.backend.auth.service;

import com.tecmarket.backend.auth.entity.RefreshToken;
import com.tecmarket.backend.auth.repository.RefreshTokenRepository;
import com.tecmarket.backend.user.entity.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;
import java.util.List;
import java.util.Objects;
import com.tecmarket.backend.auth.dto.AuthSessionResponse;

@Service
public class RefreshTokenService {

    private static final int TOKEN_RANDOM_BYTES = 64;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RefreshTokenRepository refreshTokenRepository;
    private final long expirationDays;
    private final int maximumSessions;
    private final int retentionDays;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            @Value("${app.security.jwt.refresh-token-expiration-days:7}") long expirationDays,
            @Value("${app.security.sessions.maximum-per-user:5}") int maximumSessions,
            @Value("${app.security.sessions.retention-days:30}") int retentionDays) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.expirationDays = expirationDays;
        this.maximumSessions = maximumSessions;
        this.retentionDays = retentionDays;
    }

    @Transactional
    public String issue(AppUser user) {
        return issueSession(user, null, null).refreshToken();
    }

    @Transactional
    public String issue(AppUser user, String clientDescription, String ipAddress) {
        return issueSession(user, clientDescription, ipAddress).refreshToken();
    }

    @Transactional
    public IssuedSession issueSession(AppUser user, String clientDescription, String ipAddress) {
        if (expirationDays <= 0) {
            throw new IllegalStateException("Refresh token expiration must be greater than zero.");
        }
        if (maximumSessions <= 0) {
            throw new IllegalStateException("Maximum sessions must be greater than zero.");
        }

        LocalDateTime now = LocalDateTime.now();
        List<RefreshToken> activeSessions = refreshTokenRepository
                .findByUserIdAndRevokedFalseAndExpiresAtAfterOrderByCreatedAtAsc(user.getId(), now);
        int sessionsToRevoke = activeSessions.size() - maximumSessions + 1;
        for (int index = 0; index < sessionsToRevoke; index++) {
            refreshTokenRepository.revokeActiveFamily(activeSessions.get(index).getFamilyId(), now);
        }

        String rawToken = generateToken();
        UUID familyId = UUID.randomUUID();
        RefreshToken token = buildToken(
                user,
                hash(rawToken),
                familyId,
                now.plusDays(expirationDays),
                normalizeMetadata(clientDescription, 255),
                normalizeMetadata(ipAddress, 64));
        refreshTokenRepository.save(token);
        return new IssuedSession(rawToken, familyId);
    }

    @Transactional(noRollbackFor = BadCredentialsException.class)
    public RefreshSession rotate(String rawToken) {
        String tokenHash = hashNormalized(rawToken);
        RefreshToken current = refreshTokenRepository.findForUpdateByTokenHash(tokenHash)
                .orElseThrow(() -> new BadCredentialsException("auth.token.invalid"));
        LocalDateTime now = LocalDateTime.now();

        if (Boolean.TRUE.equals(current.getRevoked())) {
            refreshTokenRepository.revokeActiveFamily(current.getFamilyId(), now);
            throw new BadCredentialsException("auth.token.invalid");
        }
        if (!current.getExpiresAt().isAfter(now)) {
            revokeFamily(current, now);
            throw new BadCredentialsException("auth.token.invalid");
        }

        AppUser user = current.getUser();
        if (!Boolean.TRUE.equals(user.getActive()) || !Boolean.TRUE.equals(user.getEmailVerified())) {
            revokeFamily(current, now);
            throw new BadCredentialsException("auth.token.invalid");
        }
        if (!Objects.equals(current.getIssuedSecurityVersion(), currentSecurityVersion(user))) {
            revokeFamily(current, now);
            throw new BadCredentialsException("auth.token.invalid");
        }

        String replacementRawToken = generateToken();
        String replacementHash = hash(replacementRawToken);
        current.setRevoked(true);
        current.setRevokedAt(now);
        current.setLastUsedAt(now);
        current.setReplacedByTokenHash(replacementHash);

        RefreshToken replacement = buildToken(
                user,
                replacementHash,
                current.getFamilyId(),
                current.getExpiresAt(),
                current.getClientDescription(),
                current.getIpAddress());
        refreshTokenRepository.save(replacement);
        return new RefreshSession(user, replacementRawToken, current.getFamilyId());
    }

    @Transactional
    public void revoke(String rawToken, Integer authenticatedUserId) {
        RefreshToken token = refreshTokenRepository.findForUpdateByTokenHash(hashNormalized(rawToken))
                .orElseThrow(() -> new BadCredentialsException("auth.token.invalid"));
        if (!token.getUser().getId().equals(authenticatedUserId)) {
            throw new BadCredentialsException("auth.token.invalid");
        }
        refreshTokenRepository.revokeActiveFamily(token.getFamilyId(), LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<AuthSessionResponse> findActiveSessions(Integer userId) {
        return refreshTokenRepository
                .findByUserIdAndRevokedFalseAndExpiresAtAfterOrderByCreatedAtAsc(userId, LocalDateTime.now())
                .stream()
                .map(token -> new AuthSessionResponse(
                        token.getFamilyId(),
                        token.getClientDescription(),
                        token.getIpAddress(),
                        token.getCreatedAt(),
                        token.getLastUsedAt(),
                        token.getExpiresAt()))
                .toList();
    }

    @Transactional
    public void revokeSession(UUID familyId, Integer authenticatedUserId) {
        if (refreshTokenRepository.revokeActiveFamilyForUser(
                familyId, authenticatedUserId, LocalDateTime.now()) == 0) {
            throw new BadCredentialsException("auth.token.invalid");
        }
    }

    @Scheduled(cron = "${app.security.sessions.cleanup-cron:0 15 3 * * *}")
    @Transactional
    public void cleanupExpiredSessions() {
        refreshTokenRepository.deleteExpiredAndOldRevoked(
                LocalDateTime.now().minusDays(Math.max(1, retentionDays)));
    }

    private void revokeFamily(RefreshToken token, LocalDateTime revokedAt) {
        token.setRevoked(true);
        token.setRevokedAt(revokedAt);
        refreshTokenRepository.revokeActiveFamily(token.getFamilyId(), revokedAt);
    }

    private RefreshToken buildToken(
            AppUser user,
            String tokenHash,
            UUID familyId,
            LocalDateTime expiresAt,
            String clientDescription,
            String ipAddress) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setTokenHash(tokenHash);
        token.setFamilyId(familyId);
        token.setExpiresAt(expiresAt);
        token.setRevoked(false);
        token.setIssuedSecurityVersion(currentSecurityVersion(user));
        token.setClientDescription(clientDescription);
        token.setIpAddress(ipAddress);
        return token;
    }

    private int currentSecurityVersion(AppUser user) {
        return user.getSecurityVersion() == null ? 0 : user.getSecurityVersion();
    }

    private String normalizeMetadata(String value, int maxLength) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String normalized = value.trim().replaceAll("[\\r\\n]", " ");
        return normalized.length() <= maxLength ? normalized : normalized.substring(0, maxLength);
    }

    private String hashNormalized(String rawToken) {
        if (!StringUtils.hasText(rawToken)) {
            throw new BadCredentialsException("auth.token.invalid");
        }
        return hash(rawToken.trim());
    }

    private String generateToken() {
        byte[] bytes = new byte[TOKEN_RANDOM_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available.", exception);
        }
    }

    public record IssuedSession(String refreshToken, UUID familyId) {
    }

    public record RefreshSession(AppUser user, String refreshToken, UUID familyId) {
    }
}
