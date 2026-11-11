package com.tecmarket.backend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Set;

@Component
@Profile("prod")
public class ProductionConfigurationValidator {

    private static final Set<String> PLACEHOLDER_MARKERS = Set.of(
            "replace-with",
            "reemplaza-con",
            "change-me",
            "changeme",
            "example-secret",
            "tecmarket_password");

    private final Environment environment;

    public ProductionConfigurationValidator(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    void validate() {
        requirePostgresDatasource();
        requireSecret("spring.datasource.password", 16);
        requireSecret("spring.flyway.password", 16);
        requireSecret("app.security.jwt.secret", 32);
        requireSecret("spring.data.redis.password", 16);
        requireSecret("app.notification.resend.api-key", 12);

        requireText("spring.datasource.username");
        requireText("spring.flyway.user");
        requireDifferentValues("spring.datasource.username", "spring.flyway.user");
        requireText("spring.data.redis.host");
        requireText("app.security.jwt.issuer");
        requireText("app.security.jwt.audience");
        requireEmail("app.auth.email-verification.from");
        requireHttpsUrl("app.auth.email-verification.verify-url");
        requireHttpsUrl("app.auth.password-reset.url");
        requireHttpsUrl("app.notification.resend.base-url");
        validateCorsOrigins();
        validateTrustedProxies();

        requireValue("spring.jpa.hibernate.ddl-auto", "validate");
        requireFalse("spring.jpa.show-sql");
        requireFalse("spring.jpa.open-in-view");
        requireTrue("spring.flyway.enabled");
        requireTrue("spring.flyway.clean-disabled");
        requireTrue("spring.flyway.validate-on-migrate");
        requireTrue("app.security.rate-limit.redis-enabled");
        requireTrue("app.security.refresh-cookie.secure");
        requireTrue("app.security.refresh-cookie.require-origin");
        requireAllowedValue("app.security.refresh-cookie.same-site", Set.of("lax", "strict", "none"));
        requireIntegerRange("app.security.jwt.access-token-expiration-minutes", 5, 15);
        requireIntegerRange("app.security.jwt.refresh-token-expiration-days", 1, 30);
        requireIntegerRange("app.security.sessions.maximum-per-user", 1, 10);
        requireFalse("springdoc.api-docs.enabled");
        requireFalse("springdoc.swagger-ui.enabled");
        requireFalse("app.seed.enabled");
        requireFalse("app.seed.platform-admin.enabled");
        requireValue("server.error.include-message", "never");
        requireValue("server.error.include-binding-errors", "never");
        requireValue("server.error.include-stacktrace", "never");
        requireValue("server.forward-headers-strategy", "none");
    }

    private void requirePostgresDatasource() {
        String url = requireText("spring.datasource.url");
        if (!url.startsWith("jdbc:postgresql://")) {
            throw invalid("spring.datasource.url must use PostgreSQL in production");
        }
    }

    private void validateCorsOrigins() {
        String configuredOrigins = requireText("app.security.cors.allowed-origins");
        boolean allowHttpLocalhost = Boolean.parseBoolean(
                environment.getProperty("app.security.cors.allow-http-localhost", "false"));
        for (String configuredOrigin : configuredOrigins.split(",")) {
            String origin = configuredOrigin.trim();
            if (!StringUtils.hasText(origin) || origin.contains("*")) {
                throw invalid("CORS origins must be explicit HTTPS origins");
            }
            URI uri = parseUri(origin, "app.security.cors.allowed-origins");
            boolean invalidPath = StringUtils.hasText(uri.getPath());
            boolean loopbackHost = "localhost".equalsIgnoreCase(uri.getHost())
                    || "127.0.0.1".equals(uri.getHost());
            boolean localDevelopmentOrigin = allowHttpLocalhost
                    && "http".equalsIgnoreCase(uri.getScheme())
                    && loopbackHost;
            if ((!"https".equalsIgnoreCase(uri.getScheme()) && !localDevelopmentOrigin)
                    || !StringUtils.hasText(uri.getHost())
                    || (loopbackHost && !allowHttpLocalhost)
                    || uri.getUserInfo() != null
                    || uri.getQuery() != null
                    || uri.getFragment() != null
                    || invalidPath) {
                throw invalid("CORS origins must be explicit HTTPS origins without paths; HTTP localhost requires the explicit development opt-in");
            }
        }
    }

    private void validateTrustedProxies() {
        String configuredRanges = requireText("app.security.trusted-proxies");
        for (String configuredRange : configuredRanges.split(",")) {
            String range = configuredRange.trim();
            if (!StringUtils.hasText(range) || "0.0.0.0/0".equals(range) || "::/0".equals(range)) {
                throw invalid("Trusted proxy ranges cannot trust the entire Internet");
            }
            try {
                new IpAddressMatcher(range);
            } catch (IllegalArgumentException exception) {
                throw invalid("Invalid trusted proxy range: " + range, exception);
            }
        }
    }

    private void requireHttpsUrl(String propertyName) {
        URI uri = parseUri(requireText(propertyName), propertyName);
        if (!"https".equalsIgnoreCase(uri.getScheme()) || !StringUtils.hasText(uri.getHost())) {
            throw invalid(propertyName + " must be an absolute HTTPS URL");
        }
    }

    private void requireEmail(String propertyName) {
        String value = requireText(propertyName);
        if (value.contains("\r") || value.contains("\n") || !value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw invalid(propertyName + " must be a valid email address");
        }
    }

    private URI parseUri(String value, String propertyName) {
        try {
            return URI.create(value);
        } catch (IllegalArgumentException exception) {
            throw invalid(propertyName + " must be a valid URI", exception);
        }
    }

    private void requireSecret(String propertyName, int minimumBytes) {
        String value = requireText(propertyName);
        String normalized = value.toLowerCase(Locale.ROOT);
        boolean placeholder = PLACEHOLDER_MARKERS.stream().anyMatch(normalized::contains);
        if (placeholder || value.getBytes(StandardCharsets.UTF_8).length < minimumBytes) {
            throw invalid(propertyName + " is missing, weak, or still contains an example value");
        }
    }

    private void requireTrue(String propertyName) {
        requireValue(propertyName, "true");
    }

    private void requireFalse(String propertyName) {
        requireValue(propertyName, "false");
    }

    private void requireValue(String propertyName, String expected) {
        String actual = requireText(propertyName);
        if (!expected.equalsIgnoreCase(actual)) {
            throw invalid(propertyName + " must be " + expected + " in production");
        }
    }

    private void requireAllowedValue(String propertyName, Set<String> allowed) {
        String actual = requireText(propertyName).toLowerCase(Locale.ROOT);
        if (!allowed.contains(actual)) {
            throw invalid(propertyName + " has an unsafe production value");
        }
    }

    private void requireIntegerRange(String propertyName, int minimum, int maximum) {
        try {
            int value = Integer.parseInt(requireText(propertyName));
            if (value < minimum || value > maximum) {
                throw invalid(propertyName + " must be between " + minimum + " and " + maximum);
            }
        } catch (NumberFormatException exception) {
            throw invalid(propertyName + " must be an integer", exception);
        }
    }

    private void requireDifferentValues(String firstProperty, String secondProperty) {
        if (requireText(firstProperty).equals(requireText(secondProperty))) {
            throw invalid(firstProperty + " and " + secondProperty + " must use different principals");
        }
    }

    private String requireText(String propertyName) {
        String value = environment.getProperty(propertyName);
        if (!StringUtils.hasText(value)) {
            throw invalid(propertyName + " is required in production");
        }
        return value.trim();
    }

    private IllegalStateException invalid(String message) {
        return new IllegalStateException(message);
    }

    private IllegalStateException invalid(String message, Exception cause) {
        return new IllegalStateException(message, cause);
    }
}
