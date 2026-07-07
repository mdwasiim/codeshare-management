package com.codeshare.airline.schedule.ingestion.shared.util;

import com.codeshare.airline.schedule.ingestion.domain.enums.AsmMessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.SsmMessageType;

import java.util.ArrayList;
import java.util.List;

public final class ActionLineParser {

    private ActionLineParser() {
    }

    public static ParsedActionLine parseAsm(String line) {
        String normalized = normalize(line);
        String[] parts = normalized.split("\\s+", 2);

        String actionPart = parts[0];
        String reasonPart = parts.length > 1 ? parts[1].trim() : "";

        String[] actionTokens = actionPart.split("/");
        String primary = actionTokens[0];

        List<String> secondary = new ArrayList<>();
        for (int i = 1; i < actionTokens.length; i++) {
            if (!actionTokens[i].isBlank()) {
                secondary.add(actionTokens[i]);
            }
        }

        List<String> reasons = new ArrayList<>();
        if (!reasonPart.isBlank()) {
            for (String token : reasonPart.split("/")) {
                String trimmed = token.trim();
                if (!trimmed.isBlank()) {
                    reasons.add(trimmed);
                }
            }
        }

        return new ParsedActionLine(primary, secondary, reasons, false);
    }

    public static ParsedActionLine parseSsm(String line) {
        String normalized = normalize(line);
        String[] parts = normalized.split("\\s+");

        String primary = parts.length > 0 ? parts[0] : "";
        boolean withdrawal = false;
        List<String> extras = new ArrayList<>();

        for (int i = 1; i < parts.length; i++) {
            if ("XASM".equals(parts[i])) {
                withdrawal = true;
            } else if (!parts[i].isBlank()) {
                extras.add(parts[i]);
            }
        }

        return new ParsedActionLine(primary, List.of(), extras, withdrawal);
    }

    public static boolean isAsmAction(String line) {
        return AsmMessageType.from(parseAsm(line).primaryAction()) != null;
    }

    public static boolean isSsmAction(String line) {
        return SsmMessageType.from(parseSsm(line).primaryAction()) != null;
    }

    private static String normalize(String line) {
        return line == null ? "" : line.trim().toUpperCase();
    }

    public record ParsedActionLine(
            String primaryAction,
            List<String> secondaryActions,
            List<String> changeReasons,
            boolean asmWithdrawalIndicator
    ) {
    }
}
