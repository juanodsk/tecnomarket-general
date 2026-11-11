package com.tecmarket.backend.repairorder.service;

import java.util.Locale;

public final class RepairOrderStateMachine {

    private RepairOrderStateMachine() {
    }

    public static boolean isTransitionAllowed(String currentStatus, String targetStatus) {
        String current = normalize(currentStatus);
        String target = normalize(targetStatus);

        if (current.equals(target)) {
            return true;
        }
        if (isTerminal(current)) {
            return false;
        }
        if ("CANCELLED".equals(target)) {
            return true;
        }

        return switch (current) {
            case "RECEIVED" -> "DIAGNOSIS".equals(target);
            case "DIAGNOSIS" -> "WAITING_APPROVAL".equals(target);
            case "WAITING_APPROVAL" -> "WAITING_PARTS".equals(target) || "REPAIRING".equals(target);
            case "WAITING_PARTS" -> "REPAIRING".equals(target);
            case "REPAIRING" -> "WAITING_PARTS".equals(target) || "TESTING".equals(target);
            case "TESTING" -> "REPAIRING".equals(target) || "READY_FOR_PICKUP".equals(target);
            case "READY_FOR_PICKUP" -> "DELIVERED".equals(target);
            default -> false;
        };
    }

    public static boolean isTerminal(String status) {
        String normalized = normalize(status);
        return "DELIVERED".equals(normalized) || "CANCELLED".equals(normalized);
    }

    private static String normalize(String status) {
        return status == null ? "" : status.trim().toUpperCase(Locale.ROOT);
    }
}
