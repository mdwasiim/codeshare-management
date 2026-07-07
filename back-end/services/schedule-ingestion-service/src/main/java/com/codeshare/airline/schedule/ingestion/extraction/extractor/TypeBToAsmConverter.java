package com.codeshare.airline.schedule.ingestion.extraction.extractor;

import java.util.ArrayList;
import java.util.List;

public final class TypeBToAsmConverter {

    private TypeBToAsmConverter() {}

    public static List<String> convert(List<String> raw) {

        if (raw == null || raw.isEmpty()) {
            throw new IllegalArgumentException("Empty Type B message");
        }

        List<String> result = new ArrayList<>();
        int i = 0;

        if (isAddressLine(raw.get(i).trim())) {
            i++;
        }

        if (i >= raw.size()) {
            throw new IllegalStateException("Invalid Type B: missing message type");
        }

        String messageType = raw.get(i).trim().toUpperCase();
        if (!messageType.equals("ASM") && !messageType.equals("SSM")) {
            throw new IllegalArgumentException("Unsupported Type B message: " + messageType);
        }

        result.add(messageType);
        i++;

        String smi = safeGet(raw, i++);
        String date = safeGet(raw, i++);
        String time = safeGet(raw, i++);
        String creator = safeGet(raw, i++);
        String seq = safeGet(raw, i++);

        // Keep a parser-friendly reference but avoid inventing a time-mode line.
        String ref = date + leftPadDigits(seq, 5) + "E001/REF " + creator;
        result.add(ref);

        while (i < raw.size()) {
            String line = raw.get(i).trim();

            if (line.isEmpty()) {
                i++;
                continue;
            }

            if ("//".equals(line)) {
                result.add("//");
                break;
            }

            if (isSmi(line)) {
                i++;
                continue;
            }

            // Preserve composite flight+route lines exactly; the ASM parser can
            // parse them without splitting.
            result.add(line);
            i++;
        }

        return result;
    }

    private static boolean isAddressLine(String line) {
        return line.matches("^[A-Z]{2}[A-Z0-9]{5,6}$");
    }

    private static boolean isSmi(String line) {
        return line.matches("^S\\d+$");
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
