package com.codeshare.airline.schedule.ingestion.orchestration.parsers.shared;

import com.codeshare.airline.schedule.ingestion.domain.enums.DeiScope;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleDataElementDTO;

import java.util.regex.Pattern;

public final class DeiParser {

    private DeiParser() {}

    private static final Pattern BOARD_OFF_PATTERN =
            Pattern.compile("^([A-Z]{3}/[A-Z]{3}|[A-Z]{6})/\\d{1,3}$");

    private static final Pattern NUMERIC_PATTERN =
            Pattern.compile("^\\d{1,3}$");

    public static ScheduleDataElementDTO parse(String line) {

        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Empty DEI line");
        }

        ScheduleDataElementDTO dei = new ScheduleDataElementDTO();
        dei.setRawLine(line);

        try {
            String trimmed = normalize(line);

            String[] parts = trimmed.split("\\s+", 2);

            String main = parts[0];
            String value = parts.length > 1 ? parts[1].trim() : "";

            // =========================
            // CASE 1: BOARD/OFF LEVEL
            // =========================
            if (BOARD_OFF_PATTERN.matcher(main).matches()) {

                ParsedRoute parsed = parseRoute(main);

                dei.setBoardPoint(parsed.board);
                dei.setOffPoint(parsed.off);
                dei.setDeiCode(parsed.code);
                dei.setValue(value);

                dei.setScope(resolveScope(parsed.code));

                return dei;
            }

            // =========================
            // CASE 2: NUMERIC DEI
            // =========================
            if (NUMERIC_PATTERN.matcher(main).matches()) {

                int code = Integer.parseInt(main);

                dei.setDeiCode(code);
                dei.setValue(value);

                // 🔥 FIX: use unified logic
                dei.setScope(resolveScope(code));

                return dei;
            }

            throw new IllegalArgumentException("Unsupported DEI format: " + line);

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid DEI: " + line, e);
        }
    }

    // =========================
    // NORMALIZATION
    // =========================
    private static String normalize(String line) {
        String trimmed = line.trim().toUpperCase();
        return trimmed.startsWith("/") ? trimmed.substring(1) : trimmed;
    }

    // =========================
    // ROUTE PARSER
    // =========================
    private static ParsedRoute parseRoute(String main) {

        int lastSlash = main.lastIndexOf('/');

        String routePart = main.substring(0, lastSlash);
        int code = Integer.parseInt(main.substring(lastSlash + 1));

        String board;
        String off;

        if (routePart.contains("/")) {
            String[] route = routePart.split("/");
            board = route[0];
            off = route[1];
        } else {
            board = routePart.substring(0, 3);
            off = routePart.substring(3, 6);
        }

        return new ParsedRoute(board, off, code);
    }

    private static class ParsedRoute {
        String board;
        String off;
        int code;

        ParsedRoute(String board, String off, int code) {
            this.board = board;
            this.off = off;
            this.code = code;
        }
    }

    // =========================
    // SCOPE RESOLUTION (CORE LOGIC)
    // =========================
    private static DeiScope resolveScope(int code) {

        switch (code) {

            // SEGMENT
            case 101: case 102: case 103: case 104: case 105:
            case 106: case 107: case 108: case 109: case 110:
            case 111: case 112: case 113: case 114: case 115:
            case 120: case 121: case 122: case 123: case 124:
            case 125:
                return DeiScope.SEGMENT;

            // LEG
            case 200: case 201: case 202: case 203: case 204:
            case 205: case 206: case 207: case 208: case 209:
            case 210:
                return DeiScope.LEG;

            // FLIGHT
            case 500: case 501: case 502: case 503: case 504:
            case 505: case 506: case 507: case 508: case 509:
                return DeiScope.FLIGHT;
        }

        // 001–099
        if (code >= 1 && code <= 99) {
            return DeiScope.LEG;
        }

        // 100–199
        if (code >= 100 && code <= 199) {
            return DeiScope.SEGMENT;
        }

        // 200–499
        if (code >= 200 && code <= 499) {
            return DeiScope.LEG;
        }

        // 500–599
        if (code >= 500 && code <= 599) {
            return DeiScope.FLIGHT;
        }

        // 600–999 (custom)
        if (code >= 600 && code <= 999) {
            return DeiScope.LEG;
        }

        return DeiScope.LEG;
    }
}