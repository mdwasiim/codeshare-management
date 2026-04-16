package com.codeshare.airline.ingestion.orchestration.parsers.shared;

import com.codeshare.airline.ingestion.persistence.dto.common.base.ScheduleIdentityDTO;

import java.util.regex.Pattern;

public final class FlightIdentityParser {

    private FlightIdentityParser() {}

    // Strict flight pattern (common)
    private static final Pattern FLIGHT_PATTERN = Pattern.compile("^([A-Z]{2})(\\d{1,4})([A-Z]?)$");

    /* =========================================================
       ✈️ ASM PARSER
       Example: QR123 DOHLHR 15APR
       ========================================================= */
    public static ScheduleIdentityDTO parseAsm(String line) {

        ScheduleIdentityDTO dto = new ScheduleIdentityDTO();

        if (line == null || line.isBlank()) return dto;

        String[] parts = line.trim().toUpperCase().split("\\s+");

        if (parts.length == 0) return dto;

        // 1️⃣ Flight (MANDATORY)
        parseFlight(parts[0], dto);

        // 2️⃣ Flexible parsing (order-independent)
        for (int i = 1; i < parts.length; i++) {

            String part = parts[i];

            if (part.matches("^[A-Z]{6}$")) {
                parseRoute(part, dto);
            }
            else if (part.matches("^\\d{2}[A-Z]{3}$")) {
                dto.setOperationDate(part);
            }
        }

        return dto;
    }

    /* =========================================================
       ✈️ SSM PARSER
       Example: QR123 01APR30SEP 1234567 DOHLHR
       ========================================================= */
    public static ScheduleIdentityDTO parseSsm(String line) {

        ScheduleIdentityDTO dto = new ScheduleIdentityDTO();

        if (line == null || line.isBlank()) return dto;

        String[] parts = line.trim().toUpperCase().split("\\s+");

        if (parts.length == 0) return dto;

        // 1️⃣ Flight only (rest handled by other parsers)
        parseFlight(parts[0], dto);

        return dto;
    }

    /* =========================================================
       🔧 COMMON HELPERS
       ========================================================= */

    private static void parseFlight(String value, ScheduleIdentityDTO dto) {

        var matcher = FLIGHT_PATTERN.matcher(value);

        if (!matcher.matches()) {
            return; // invalid format
        }

        dto.setAirlineDesignator(matcher.group(1));
        dto.setFlightNumber(matcher.group(2));

        String suffix = matcher.group(3);
        if (suffix != null && !suffix.isBlank()) {
            dto.setOperationalSuffix(suffix);
        }
    }

    private static void parseRoute(String route, ScheduleIdentityDTO dto) {

        if (route != null && route.matches("^[A-Z]{6}$")) {
            dto.setBoardPoint(route.substring(0, 3));
            dto.setOffPoint(route.substring(3, 6));
        }
    }
}