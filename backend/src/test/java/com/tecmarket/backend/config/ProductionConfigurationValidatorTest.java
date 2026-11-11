package com.tecmarket.backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductionConfigurationValidatorTest {

    @Test
    void acceptsHardenedProductionConfiguration() {
        assertThatCode(() -> validator(validEnvironment()).validate())
                .doesNotThrowAnyException();
    }

    @Test
    void rejectsWildcardOrInsecureCorsOrigins() {
        MockEnvironment environment = validEnvironment()
                .withProperty("app.security.cors.allowed-origins", "http://*.example.com");

        assertThatThrownBy(() -> validator(environment).validate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("CORS origins");
    }

    @Test
    void acceptsExplicitCrossSiteLocalDevelopmentConfiguration() {
        MockEnvironment environment = validEnvironment()
                .withProperty("app.security.cors.allowed-origins", "http://localhost:3000")
                .withProperty("app.security.cors.allow-http-localhost", "true")
                .withProperty("app.security.refresh-cookie.same-site", "None");

        assertThatCode(() -> validator(environment).validate())
                .doesNotThrowAnyException();
    }

    @Test
    void rejectsLocalhostCorsWithoutExplicitDevelopmentOptIn() {
        MockEnvironment environment = validEnvironment()
                .withProperty("app.security.cors.allowed-origins", "http://localhost:3000");

        assertThatThrownBy(() -> validator(environment).validate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("development opt-in");
    }

    @Test
    void rejectsWeakExampleSecrets() {
        String placeholder = String.join("-", "replace", "with", "placeholder");
        MockEnvironment environment = validEnvironment()
                .withProperty("app.security.jwt.secret", placeholder);

        assertThatThrownBy(() -> validator(environment).validate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("example value");
    }

    @Test
    void rejectsUnsafeRuntimeOverrides() {
        MockEnvironment environment = validEnvironment()
                .withProperty("springdoc.api-docs.enabled", "true");

        assertThatThrownBy(() -> validator(environment).validate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("must be false");
    }

    @Test
    void rejectsProxyRangesThatTrustTheInternet() {
        MockEnvironment environment = validEnvironment()
                .withProperty("app.security.trusted-proxies", "0.0.0.0/0");

        assertThatThrownBy(() -> validator(environment).validate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("entire Internet");
    }

    private ProductionConfigurationValidator validator(MockEnvironment environment) {
        return new ProductionConfigurationValidator(environment);
    }

    private MockEnvironment validEnvironment() {
        return new MockEnvironment()
                .withProperty("spring.datasource.url", "jdbc:postgresql://postgres:5432/tecmarket")
                .withProperty("spring.datasource.username", "tecmarket_app")
                .withProperty("spring.datasource.password", "a-real-database-password-123")
                .withProperty("spring.flyway.user", "tecmarket_migrator")
                .withProperty("spring.flyway.password", "a-real-migration-password-123")
                .withProperty("spring.jpa.hibernate.ddl-auto", "validate")
                .withProperty("spring.jpa.show-sql", "false")
                .withProperty("spring.jpa.open-in-view", "false")
                .withProperty("spring.flyway.enabled", "true")
                .withProperty("spring.flyway.clean-disabled", "true")
                .withProperty("spring.flyway.validate-on-migrate", "true")
                .withProperty("app.security.jwt.secret", "a-real-jwt-secret-with-more-than-32-bytes")
                .withProperty("app.security.jwt.issuer", "tecmarket")
                .withProperty("app.security.jwt.audience", "tecmarket-api")
                .withProperty("app.security.jwt.access-token-expiration-minutes", "15")
                .withProperty("app.security.jwt.refresh-token-expiration-days", "7")
                .withProperty("app.security.sessions.maximum-per-user", "5")
                .withProperty("app.security.cors.allowed-origins", "https://app.tecmarket.test")
                .withProperty("app.security.trusted-proxies", "172.16.0.0/12")
                .withProperty("app.security.rate-limit.redis-enabled", "true")
                .withProperty("app.security.refresh-cookie.secure", "true")
                .withProperty("app.security.refresh-cookie.require-origin", "true")
                .withProperty("app.security.refresh-cookie.same-site", "Lax")
                .withProperty("spring.data.redis.host", "redis")
                .withProperty("spring.data.redis.password", "a-real-redis-password-123")
                .withProperty("app.auth.email-verification.verify-url", "https://app.tecmarket.test/verify-email")
                .withProperty("app.auth.email-verification.from", "no-reply@tecmarket.test")
                .withProperty("app.auth.password-reset.url", "https://app.tecmarket.test/reset-password")
                .withProperty("app.notification.resend.api-key", "re_1234567890_secure")
                .withProperty("app.notification.resend.base-url", "https://api.resend.com")
                .withProperty("springdoc.api-docs.enabled", "false")
                .withProperty("springdoc.swagger-ui.enabled", "false")
                .withProperty("app.seed.enabled", "false")
                .withProperty("app.seed.platform-admin.enabled", "false")
                .withProperty("server.error.include-message", "never")
                .withProperty("server.error.include-binding-errors", "never")
                .withProperty("server.error.include-stacktrace", "never")
                .withProperty("server.forward-headers-strategy", "none");
    }
}
