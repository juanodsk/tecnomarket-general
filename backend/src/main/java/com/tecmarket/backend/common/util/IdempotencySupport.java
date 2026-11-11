package com.tecmarket.backend.common.util;

import com.tecmarket.backend.common.exception.BadRequestException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.regex.Pattern;

public final class IdempotencySupport {

    private static final Pattern KEY_PATTERN = Pattern.compile("^[A-Za-z0-9._:-]{8,100}$");

    private IdempotencySupport() {
    }

    public static String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String normalized = value.trim();
        if (!KEY_PATTERN.matcher(normalized).matches()) {
            throw new BadRequestException("idempotency.key.invalid");
        }
        return normalized;
    }

    public static String fingerprint(Object request) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(String.valueOf(request).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available.", exception);
        }
    }

    public static String fingerprint(String actorSubject, Object request) {
        return fingerprint(String.valueOf(actorSubject) + "\n" + String.valueOf(request));
    }
}
