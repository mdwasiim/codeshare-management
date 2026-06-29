package com.codeshare.airline.inbound.orchestration.parsers.shared;

import com.codeshare.airline.inbound.dto.common.base.ScheduleIdentityDTO;

import java.util.regex.Pattern;

public final class FlightIdentityParser {

    private FlightIdentityParser() {}

    // Strict flight pattern (common)
    private static final Pattern FLIGHT_PATTERN = Pattern.compile("^([A-Z0-9]{2})(\\d{1,4})([A-Z]?)$");

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
        if (parts[0].contains("/")) {
            parseAsmFlightIdentifier(parts[0], dto);
        } else {
            parseFlight(parts[0], dto);
        }

        // 2️⃣ Flexible parsing (order-independent)
        for (int i = 1; i < parts.length; i++) {

            String part = parts[i];

            if (part.matches("^[A-Z]{6}$")) {
                parseRoute(part, dto);
            }
            else if (part.matches("^\\d{2}[A-Z]{3}(\\d{2})?$")) {
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

    private static void parseAsmFlightIdentifier(String value, ScheduleIdentityDTO dto) {
        String[] split = value.split("/", 2);
        parseFlight(split[0], dto);
        if (split.length == 2 && !split[1].isBlank()) {
            dto.setOperationDate(split[1]);
        }
    }

    private static void parseRoute(String route, ScheduleIdentityDTO dto) {

        if (route != null && route.matches("^[A-Z]{6}$")) {
            dto.setBoardPoint(route.substring(0, 3));
            dto.setOffPoint(route.substring(3, 6));
        }
    }
}
