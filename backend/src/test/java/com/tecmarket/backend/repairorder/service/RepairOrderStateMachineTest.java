package com.tecmarket.backend.repairorder.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RepairOrderStateMachineTest {

    @Test
    void acceptsTheMvpHappyPathAndOnlyTheTwoAuthorizedRegressions() {
        assertThat(RepairOrderStateMachine.isTransitionAllowed("RECEIVED", "DIAGNOSIS")).isTrue();
        assertThat(RepairOrderStateMachine.isTransitionAllowed("DIAGNOSIS", "WAITING_APPROVAL")).isTrue();
        assertThat(RepairOrderStateMachine.isTransitionAllowed("WAITING_APPROVAL", "REPAIRING")).isTrue();
        assertThat(RepairOrderStateMachine.isTransitionAllowed("REPAIRING", "TESTING")).isTrue();
        assertThat(RepairOrderStateMachine.isTransitionAllowed("TESTING", "READY_FOR_PICKUP")).isTrue();
        assertThat(RepairOrderStateMachine.isTransitionAllowed("READY_FOR_PICKUP", "DELIVERED")).isTrue();
        assertThat(RepairOrderStateMachine.isTransitionAllowed("TESTING", "REPAIRING")).isTrue();
        assertThat(RepairOrderStateMachine.isTransitionAllowed("REPAIRING", "WAITING_PARTS")).isTrue();
    }

    @Test
    void rejectsSkippedStatesAndAnyTransitionOutOfTerminalStates() {
        assertThat(RepairOrderStateMachine.isTransitionAllowed("RECEIVED", "DELIVERED")).isFalse();
        assertThat(RepairOrderStateMachine.isTransitionAllowed("DIAGNOSIS", "TESTING")).isFalse();
        assertThat(RepairOrderStateMachine.isTransitionAllowed("DELIVERED", "REPAIRING")).isFalse();
        assertThat(RepairOrderStateMachine.isTransitionAllowed("CANCELLED", "RECEIVED")).isFalse();
    }

    @Test
    void allowsCancellationOnlyWhileTheOrderIsOpen() {
        assertThat(RepairOrderStateMachine.isTransitionAllowed("REPAIRING", "CANCELLED")).isTrue();
        assertThat(RepairOrderStateMachine.isTransitionAllowed("DELIVERED", "CANCELLED")).isFalse();
    }
}
