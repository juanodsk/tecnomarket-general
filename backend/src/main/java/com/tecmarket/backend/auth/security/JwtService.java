/*
 * TecMarket Backend
 * Module: Auth
 */

package com.tecmarket.backend.auth.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.tecmarket.backend.user.entity.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class JwtService {

    private static final MacAlgorithm SIGNATURE_ALGORITHM = MacAlgorithm.HS256;
    private static final int MIN_HS256_SECRET_BYTES = 32;

    private final String jwtSecret;
    private final String issuer;
    private final String audience;
    private final long accessTokenExpirationMinutes;

    private volatile JwtEncoder jwtEncoder;

    public JwtService(
            @Value("${app.security.jwt.secret:}") String jwtSecret,
            @Value("${app.security.jwt.issuer:tecmarket}") String issuer,
            @Value("${app.security.jwt.audience:tecmarket-api}") String audience,
            @Value("${app.security.jwt.access-token-expiration-minutes:15}") long accessTokenExpirationMinutes) {
        this.jwtSecret = jwtSecret;
        this.issuer = issuer;
        this.audience = audience;
        this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
    }

    public String generateAccessToken(
            AppUser user,
            Collection<String> authorities,
            UUID sessionId) {
        UUID requiredSessionId = Objects.requireNonNull(sessionId, "sessionId is required");
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(accessTokenTtl());

        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .audience(List.of(audience))
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("surname", user.getSurname())
                .claim("active", Boolean.TRUE.equals(user.getActive()))
                .claim("security_version", currentSecurityVersion(user))
                .claim("sid", requiredSessionId.toString());

        List<String> normalizedAuthorities = normalizeAuthorities(authorities);
        if (!normalizedAuthorities.isEmpty()) {
            claims.claim("authorities", normalizedAuthorities);
        }

        JwsHeader header = JwsHeader.with(SIGNATURE_ALGORITHM).build();

        return jwtEncoder()
                .encode(JwtEncoderParameters.from(header, claims.build()))
                .getTokenValue();
    }

    public long getAccessTokenExpiresInSeconds() {
        return accessTokenTtl().toSeconds();
    }

    private List<String> normalizeAuthorities(Collection<String> authorities) {
        if (authorities == null) {
            return List.of();
        }

        return authorities.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(authority -> !authority.isBlank())
                .distinct()
                .toList();
    }

    private Duration accessTokenTtl() {
        if (accessTokenExpirationMinutes <= 0) {
            throw new IllegalStateException("JWT access token expiration must be greater than zero.");
        }

        return Duration.ofMinutes(accessTokenExpirationMinutes);
    }

    private int currentSecurityVersion(AppUser user) {
        return user.getSecurityVersion() == null ? 0 : user.getSecurityVersion();
    }

    private JwtEncoder jwtEncoder() {
        JwtEncoder currentEncoder = jwtEncoder;
        if (currentEncoder == null) {
            synchronized (this) {
                currentEncoder = jwtEncoder;
                if (currentEncoder == null) {
                    currentEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(signingKey()));
                    jwtEncoder = currentEncoder;
                }
            }
        }

        return currentEncoder;
    }

    private SecretKey signingKey() {
        String secret = jwtSecret == null ? "" : jwtSecret.trim();
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);

        if (secretBytes.length < MIN_HS256_SECRET_BYTES) {
            throw new IllegalStateException("JWT secret must have at least 32 bytes for HS256.");
        }

        return new SecretKeySpec(secretBytes, SIGNATURE_ALGORITHM.getName());
    }
}
