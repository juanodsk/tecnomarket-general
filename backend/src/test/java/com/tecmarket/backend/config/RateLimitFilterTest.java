package com.tecmarket.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

class RateLimitFilterTest {

    @Test
    void ignoresSpoofedForwardedForFromAnUntrustedRemoteAddress() throws Exception {
        RateLimitFilter filter = new RateLimitFilter(
                mock(org.springframework.data.redis.core.StringRedisTemplate.class),
                new ObjectMapper(), false, "10.0.0.0/8", false, "https://app.example.test");

        for (int index = 0; index < 11; index++) {
            MockHttpServletRequest request = loginRequest("203.0.113.10");
            request.addHeader("X-Forwarded-For", "198.51.100." + index);
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request, response, (req, res) -> { });
            if (index < 10) {
                assertThat(response.getStatus()).isEqualTo(200);
            } else {
                assertThat(response.getStatus()).isEqualTo(429);
                assertThat(response.getHeader("Retry-After")).isEqualTo("60");
            }
        }
    }

    @Test
    void ignoresNonLiteralForwardedForFromATrustedProxy() throws Exception {
        RateLimitFilter filter = new RateLimitFilter(
                mock(org.springframework.data.redis.core.StringRedisTemplate.class),
                new ObjectMapper(), false, "10.0.0.0/8", false, "https://app.example.test");

        for (int index = 0; index < 11; index++) {
            MockHttpServletRequest request = loginRequest("10.0.0.5");
            request.addHeader("X-Forwarded-For", "attacker" + index + ".example.test");
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request, response, (req, res) -> { });

            assertThat(response.getStatus()).isEqualTo(index < 10 ? 200 : 429);
        }
    }

    @Test
    void rejectsRefreshWithoutAnAllowedOriginWhenCookieProtectionIsEnabled() throws Exception {
        RateLimitFilter filter = new RateLimitFilter(
                mock(org.springframework.data.redis.core.StringRedisTemplate.class),
                new ObjectMapper(), false, "", true, "https://app.example.test");
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/auth/refresh");
        request.setRemoteAddr("203.0.113.10");
        request.addHeader("Origin", "https://evil.example.test");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, (req, res) -> { });

        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentAsString()).contains("auth.origin.invalid");
    }

    @Test
    void limitsNormalizedAccountAcrossDifferentIpAddresses() throws Exception {
        RateLimitFilter filter = new RateLimitFilter(
                mock(org.springframework.data.redis.core.StringRedisTemplate.class),
                new ObjectMapper(), false, "", false, "https://app.example.test");

        for (int index = 0; index < 11; index++) {
            MockHttpServletRequest request = loginRequest("203.0.113." + index);
            request.setContentType("application/json");
            request.setContent("{\"email\":\"  USER@Example.COM  \",\"password\":\"secret\"}"
                    .getBytes(StandardCharsets.UTF_8));
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request, response, (req, res) -> { });

            assertThat(response.getStatus()).isEqualTo(index < 10 ? 200 : 429);
        }
    }

    @Test
    void preservesAuthJsonBodyAfterExtractingEmail() throws Exception {
        RateLimitFilter filter = new RateLimitFilter(
                mock(org.springframework.data.redis.core.StringRedisTemplate.class),
                new ObjectMapper(), false, "", false, "https://app.example.test");
        MockHttpServletRequest request = loginRequest("203.0.113.10");
        request.setContentType("application/json");
        request.setContent("{\"email\":\"user@example.com\",\"password\":\"secret\"}"
                .getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicReference<String> downstreamBody = new AtomicReference<>();

        filter.doFilter(request, response, (req, res) -> downstreamBody.set(
                new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8)));

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(downstreamBody.get()).contains("user@example.com", "secret");
    }

    @Test
    void rejectsOversizedAuthJsonBeforeBufferingItForAccountLimits() throws Exception {
        RateLimitFilter filter = new RateLimitFilter(
                mock(org.springframework.data.redis.core.StringRedisTemplate.class),
                new ObjectMapper(), false, "", false, "https://app.example.test");
        MockHttpServletRequest request = loginRequest("203.0.113.10");
        request.setContentType("application/json");
        request.setContent(("{\"email\":\"user@example.com\",\"padding\":\""
                + "x".repeat(17_000)
                + "\"}").getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, (req, res) -> { });

        assertThat(response.getStatus()).isEqualTo(413);
        assertThat(response.getContentAsString()).contains("request.body.too_large");
    }

    private MockHttpServletRequest loginRequest(String remoteAddress) {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/auth/login");
        request.setRemoteAddr(remoteAddress);
        return request;
    }
}
