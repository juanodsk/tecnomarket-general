package com.tecmarket.backend.common.util;

import com.tecmarket.backend.common.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IdempotencySupportTest {

    @Test
    void normalizesAllowedKeysAndRejectsUnsafeValues() {
        assertThat(IdempotencySupport.normalize("  sale:tenant-123  ")).isEqualTo("sale:tenant-123");
        assertThat(IdempotencySupport.normalize(" ")).isNull();
        assertThatThrownBy(() -> IdempotencySupport.normalize("short"))
                .isInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> IdempotencySupport.normalize("sale key with spaces"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void fingerprintIsStableAndRequestSensitive() {
        assertThat(IdempotencySupport.fingerprint("request-a"))
                .hasSize(64)
                .isEqualTo(IdempotencySupport.fingerprint("request-a"))
                .isNotEqualTo(IdempotencySupport.fingerprint("request-b"));
    }

    @Test
    void actorScopedFingerprintPreventsOneEmployeeFromReplayingAnotherEmployeesRequest() {
        assertThat(IdempotencySupport.fingerprint("41", "same-request"))
                .isEqualTo(IdempotencySupport.fingerprint("41", "same-request"))
                .isNotEqualTo(IdempotencySupport.fingerprint("42", "same-request"));
    }
}
