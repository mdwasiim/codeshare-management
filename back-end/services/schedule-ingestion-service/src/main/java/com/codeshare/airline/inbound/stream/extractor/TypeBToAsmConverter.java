package com.codeshare.airline.inbound.stream.extractor;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class TypeBToAsmConverter {

    private TypeBToAsmConverter() {}

    public static List<String> convert(List<String> raw) {

        if (raw == null || raw.isEmpty()) {
            throw new IllegalArgumentException("Empty Type B message");
        }

        List<String> result = new ArrayList<>();

        int i = 0;

        /* =========================================================
           1. SKIP ADDRESS LINE (e.g. QKDOHQR)
           ========================================================= */
        if (isAddressLine(raw.get(i))) {
            i++;
        }

        /* =========================================================
           2. MESSAGE TYPE (ASM / SSM)
           ========================================================= */
        if (i >= raw.size()) {
            throw new IllegalStateException("Invalid Type B: missing message type");
        }

        String messageType = raw.get(i).trim().toUpperCase();

        if (!messageType.equals("ASM") && !messageType.equals("SSM")) {
            throw new IllegalArgumentException("Unsupported Type B message: " + messageType);
        }

        result.add(messageType);
        i++;

        /* =========================================================
           3. HEADER (S1, DATE, TIME, CREATOR, SEQ)
           ========================================================= */

        String smi     = safeGet(raw, i++); // S1
        String date    = safeGet(raw, i++); // 24MAY
        String time    = safeGet(raw, i++); // 1200
        String creator = safeGet(raw, i++); // QRH
        String seq     = safeGet(raw, i++); // 001

        String ref = date + leftPadDigits(seq, 5) + "E001/REF " + creator;

        result.add("UTC");    // IATA default when time mode is omitted
        result.add(ref);

        /* =========================================================
           4. BODY NORMALIZATION
           ========================================================= */

        while (i < raw.size()) {

            String line = raw.get(i).trim();

            if (line.isEmpty()) {
                i++;
                continue;
            }

            // End of message
            if ("//".equals(line)) {
                result.add("//");
                break;
            }

            // Skip SMI continuation like S2, S3
            if (isSmi(line)) {
                i++;
                continue;
            }

            // 🔥 Split combined flight + routing
            if (isCombinedFlight(line)) {
                splitFlight(line, result);
                i++;
                continue;
            }

            // Normal line
            result.add(line);
            i++;
        }

        return result;
    }

    /* =========================================================
       HELPERS
       ========================================================= */

    private static boolean isAddressLine(String line) {
        return line.matches("^[A-Z]{2}[A-Z0-9]{5,6}$");
    }

    private static boolean isSmi(String line) {
        return line.matches("^S\\d+$"); // S1, S2, etc.
    }

    private static boolean isCombinedFlight(String line) {
        return line.matches("^[A-Z]{2}\\d+[A-Z]?\\s+[A-Z]{6}$");
        // QR100 DOHFRA
    }

    private static void splitFlight(String line, List<String> result) {

        try {
            String[] parts = line.split("\\s+");

            String flight = parts[0];     // QR100
            String routing = parts[1];    // DOHFRA

            result.add(flight);

            // Optional: extract board/off if needed later
            // String board = routing.substring(0, 3);
            // String off = routing.substring(3, 6);

        } catch (Exception e) {
            log.warn("Failed to split flight line: {}", line);
            result.add(line);
        }
    }

    private static String safeGet(List<String> raw, int index) {
        if (index >= raw.size()) {
            throw new IllegalStateException("Invalid Type B header structure");
        }
        return raw.get(index).trim();
    }

    private static String leftPadDigits(String value, int length) {
        String digits = value == null ? "" : value.replaceAll("\\D", "");
        if (digits.length() > length) {
            return digits.substring(digits.length() - length);
        }
        return "0".repeat(length - digits.length()) + digits;
    }
}
