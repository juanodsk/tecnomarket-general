package com.tecmarket.backend.quotation.service;

import java.util.Locale;

public final class QuotationStateMachine {

    private QuotationStateMachine() {
    }

    public static boolean isTransitionAllowed(String currentStatus, String targetStatus) {
        String current = normalize(currentStatus);
        String target = normalize(targetStatus);

        if (current.equals(target)) {
            return true;
        }

        return switch (current) {
            case "DRAFT" -> "PENDING".equals(target) || "CANCELLED".equals(target);
            case "PENDING" -> "APPROVED".equals(target)
                    || "REJECTED".equals(target)
                    || "EXPIRED".equals(target)
                    || "CANCELLED".equals(target);
            default -> false;
        };
    }

    public static boolean isTerminal(String status) {
        String normalized = normalize(status);
        return "APPROVED".equals(normalized)
                || "REJECTED".equals(normalized)
                || "EXPIRED".equals(normalized)
                || "CANCELLED".equals(normalized);
    }

    private static String normalize(String status) {
        return status == null ? "" : status.trim().toUpperCase(Locale.ROOT);
    }
}
