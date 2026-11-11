package com.tecmarket.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecmarket.backend.common.response.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final long WINDOW_SECONDS = 60;
    private static final int GLOBAL_IP_LIMIT = 120;
    private static final int GLOBAL_ACCOUNT_LIMIT = 30;
    private static final int GLOBAL_IP_ACCOUNT_LIMIT = 20;

    private final Map<String, RequestInfo> localRequests = new ConcurrentHashMap<>();
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final boolean redisEnabled;
    private final boolean requireRefreshOrigin;
    private final List<String> allowedOrigins;
    private final List<IpAddressMatcher> trustedProxies;

    public RateLimitFilter(
            StringRedisTemplate redisTemplate,
            ObjectMapper objectMapper,
            @Value("${app.security.rate-limit.redis-enabled:false}") boolean redisEnabled,
            @Value("${app.security.trusted-proxies:}") String trustedProxyRanges,
            @Value("${app.security.refresh-cookie.require-origin:false}") boolean requireRefreshOrigin,
            @Value("${app.security.cors.allowed-origins:http://localhost:3000}") String allowedOrigins) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.redisEnabled = redisEnabled;
        this.requireRefreshOrigin = requireRefreshOrigin;
        this.allowedOrigins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .toList();
        this.trustedProxies = Arrays.stream(trustedProxyRanges.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(IpAddressMatcher::new)
                .toList();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (requiresTrustedOrigin(request) && !hasTrustedOrigin(request)) {
            writeError(response, HttpStatus.FORBIDDEN, "auth.origin.invalid", false);
            return;
        }
        LimitPolicy policy = policyFor(request);
        if (policy == null) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpServletRequest effectiveRequest = request;
        String normalizedEmail = null;
        if (policy.emailAware() && request.getContentLengthLong() != 0) {
            CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
            if (cachedRequest.isTooLarge()) {
                writeError(response, HttpStatus.PAYLOAD_TOO_LARGE, "request.body.too_large", false);
                return;
            }
            effectiveRequest = cachedRequest;
            normalizedEmail = extractNormalizedEmail(cachedRequest);
        }

        String clientIp = resolveClientIp(request);
        try {
            if (exceedsAnyLimit(policy, clientIp, normalizedEmail)) {
                writeError(response, HttpStatus.TOO_MANY_REQUESTS, "rate_limit.exceeded", true);
                return;
            }
        } catch (RuntimeException exception) {
            if (redisEnabled) {
                writeError(response, HttpStatus.SERVICE_UNAVAILABLE, "rate_limit.unavailable", false);
                return;
            }
            throw exception;
        }

        filterChain.doFilter(effectiveRequest, response);
    }

    private boolean exceedsAnyLimit(LimitPolicy policy, String clientIp, String normalizedEmail) {
        List<LimitKey> limits = new ArrayList<>();
        String hashedIp = hashKey(clientIp);
        limits.add(new LimitKey("ip:" + hashedIp, GLOBAL_IP_LIMIT));
        limits.add(new LimitKey("operation:" + policy.bucket(), policy.operationLimit()));
        limits.add(new LimitKey(
                "operation-ip:" + policy.bucket() + ":" + hashedIp,
                policy.ipLimit()));
        if (StringUtils.hasText(normalizedEmail)) {
            String hashedAccount = hashKey(normalizedEmail);
            limits.add(new LimitKey("account:" + hashedAccount, GLOBAL_ACCOUNT_LIMIT));
            limits.add(new LimitKey(
                    "operation-account:" + policy.bucket() + ":" + hashedAccount,
                    policy.accountLimit()));
            String hashedCombination = hashKey(clientIp + "\n" + normalizedEmail);
            limits.add(new LimitKey(
                    "ip-account:" + hashedCombination,
                    GLOBAL_IP_ACCOUNT_LIMIT));
            limits.add(new LimitKey(
                    "operation-ip-account:" + policy.bucket() + ":" + hashedCombination,
                    policy.combinationLimit()));
        }

        for (LimitKey limit : limits) {
            if (limit.limit() <= 0) {
                continue;
            }
            String key = "tecmarket:rate-limit:" + limit.key();
            long count = redisEnabled ? incrementRedis(key) : incrementLocal(key);
            if (count > limit.limit()) {
                return true;
            }
        }
        return false;
    }

    private String extractNormalizedEmail(CachedBodyHttpServletRequest request) {
        try {
            byte[] body = request.getCachedBody();
            if (body.length == 0) {
                return null;
            }
            String email = objectMapper.readTree(body).path("email").asText(null);
            return StringUtils.hasText(email) ? email.trim().toLowerCase(java.util.Locale.ROOT) : null;
        } catch (RuntimeException | IOException exception) {
            return null;
        }
    }

    private String hashKey(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available.", exception);
        }
    }

    private long incrementRedis(String key) {
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == null) {
            throw new IllegalStateException("Redis did not return a rate-limit counter.");
        }
        if (count == 1L) {
            redisTemplate.expire(key, WINDOW_SECONDS, TimeUnit.SECONDS);
        }
        return count;
    }

    private long incrementLocal(String key) {
        long now = System.currentTimeMillis();
        localRequests.entrySet().removeIf(entry -> now - entry.getValue().timestamp() > WINDOW_SECONDS * 1000);
        return localRequests.compute(key, (ignored, current) -> {
            if (current == null || now - current.timestamp() > WINDOW_SECONDS * 1000) {
                return new RequestInfo(now, 1);
            }
            return new RequestInfo(current.timestamp(), current.count() + 1);
        }).count();
    }

    private String resolveClientIp(HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();
        if (!isTrustedProxy(remoteAddress)) {
            return remoteAddress;
        }

        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (!StringUtils.hasText(forwardedFor)) {
            return remoteAddress;
        }

        String candidate = forwardedFor.split(",")[0].trim();
        return isIpLiteral(candidate) ? candidate : remoteAddress;
    }

    private boolean isTrustedProxy(String remoteAddress) {
        return trustedProxies.stream().anyMatch(matcher -> matcher.matches(remoteAddress));
    }

    private boolean isIpLiteral(String value) {
        if (value.matches("[0-9]{1,3}(\\.[0-9]{1,3}){3}")) {
            return Arrays.stream(value.split("\\."))
                    .mapToInt(Integer::parseInt)
                    .allMatch(octet -> octet <= 255);
        }
        if (!value.contains(":") || !value.matches("[0-9a-fA-F:.]+")) {
            return false;
        }
        try {
            return InetAddress.getByName(value).getHostAddress().contains(":");
        } catch (Exception exception) {
            return false;
        }
    }

    private LimitPolicy policyFor(HttpServletRequest request) {
        String path = request.getRequestURI();
        if ("/api/v1/auth/login".equals(path)) return new LimitPolicy("auth-login", 10, 10, 1000, 5, true);
        if ("/api/v1/auth/register".equals(path)) return new LimitPolicy("auth-register", 5, 3, 300, 3, true);
        if ("/api/v1/auth/refresh".equals(path)) return new LimitPolicy("auth-refresh", 30, 0, 3000, 0, false);
        if ("/api/v1/auth/verify-email".equals(path)) return new LimitPolicy("auth-verify", 10, 0, 1000, 0, false);
        if ("/api/v1/auth/password-reset/request".equals(path)) return new LimitPolicy("password-reset-request", 5, 3, 500, 3, true);
        if ("/api/v1/auth/password-reset/confirm".equals(path)) return new LimitPolicy("password-reset-confirm", 10, 0, 1000, 0, false);
        if (path.startsWith("/api/v1/public/")) return new LimitPolicy("public-api", 120, 0, 10000, 0, false);
        if ("POST".equals(request.getMethod()) && path.endsWith("/attachments")) {
            return new LimitPolicy("repair-evidence-upload", 20, 0, 2000, 0, false);
        }
        return null;
    }

    private boolean requiresTrustedOrigin(HttpServletRequest request) {
        if (!requireRefreshOrigin || !"POST".equals(request.getMethod())) {
            return false;
        }
        String path = request.getRequestURI();
        return "/api/v1/auth/refresh".equals(path) || "/api/v1/auth/logout".equals(path);
    }

    private boolean hasTrustedOrigin(HttpServletRequest request) {
        String origin = request.getHeader(HttpHeaders.ORIGIN);
        return StringUtils.hasText(origin) && allowedOrigins.contains(origin.trim());
    }

    private void writeError(
            HttpServletResponse response,
            HttpStatus status,
            String message,
            boolean retryable) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        if (retryable) {
            response.setHeader(HttpHeaders.RETRY_AFTER, Long.toString(WINDOW_SECONDS));
        }
        objectMapper.writeValue(response.getWriter(), ApiResponse.error(message));
    }

    private record RequestInfo(long timestamp, long count) {
    }

    private record LimitPolicy(
            String bucket,
            int ipLimit,
            int accountLimit,
            int operationLimit,
            int combinationLimit,
            boolean emailAware) {
    }

    private record LimitKey(String key, int limit) {
    }
}
