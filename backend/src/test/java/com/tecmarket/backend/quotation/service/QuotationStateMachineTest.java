package com.tecmarket.backend.quotation.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuotationStateMachineTest {

    @Test
    void acceptsOnlyTheMvpQuotationLifecycle() {
        assertThat(QuotationStateMachine.isTransitionAllowed("DRAFT", "PENDING")).isTrue();
        assertThat(QuotationStateMachine.isTransitionAllowed("PENDING", "APPROVED")).isTrue();
        assertThat(QuotationStateMachine.isTransitionAllowed("PENDING", "REJECTED")).isTrue();
        assertThat(QuotationStateMachine.isTransitionAllowed("PENDING", "EXPIRED")).isTrue();
        assertThat(QuotationStateMachine.isTransitionAllowed("DRAFT", "CANCELLED")).isTrue();
    }

    @Test
    void rejectsSkippedAndTerminalTransitions() {
        assertThat(QuotationStateMachine.isTransitionAllowed("DRAFT", "APPROVED")).isFalse();
        assertThat(QuotationStateMachine.isTransitionAllowed("APPROVED", "REJECTED")).isFalse();
        assertThat(QuotationStateMachine.isTransitionAllowed("REJECTED", "PENDING")).isFalse();
        assertThat(QuotationStateMachine.isTransitionAllowed("EXPIRED", "APPROVED")).isFalse();
        assertThat(QuotationStateMachine.isTransitionAllowed("CANCELLED", "PENDING")).isFalse();
    }
}
