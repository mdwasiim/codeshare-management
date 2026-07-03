package com.codeshare.airline.schedule.ingestion.shared.util;

import java.util.Set;
import java.util.regex.Pattern;

public final class LineClassifierUtil {

    private LineClassifierUtil() {}

    /* ================= PATTERNS ================= */
    private static final Pattern MESSAGE_REF =
            Pattern.compile("^\\d{2}[A-Z]{3}\\d{5}[A-Z]\\d{3}(/.*)?$");

    private static final Pattern PERIOD =
            Pattern.compile("^\\d{2}[A-Z]{3}\\s?\\d{2}[A-Z]{3}(\\s+[1-7]{1,7})?$");

    private static final Pattern FLIGHT =
            Pattern.compile("^[A-Z0-9]{2}\\d{1,4}[A-Z]?$");

    private static final Pattern LEG =
            Pattern.compile("^[A-Z]{3}\\s+[A-Z]{3}(\\s+[A-Z0-9]{1,5})*$");

    private static final Pattern TIME =
            Pattern.compile("^\\d{4}(\\+\\d)?\\s+\\d{4}(\\+\\d)?$");

    private static final Pattern EQUIPMENT =
            Pattern.compile("^[A-Z]\\s+[A-Z0-9]{2,4}(\\s+\\S+)*$");

    private static final Pattern DEI_1 =
            Pattern.compile("^[A-Z]{3}/[A-Z]{3}/\\d{1,3}.*$");

    private static final Pattern DEI_2 =
            Pattern.compile("^[A-Z]{3}\\s+[A-Z]{3}\\s+\\d{1,3}/.*$");

    private static final Pattern DEI_3 =
            Pattern.compile("^/\\d{1,3}.*$");

    private static final Pattern DEI_4 =
            Pattern.compile("^[A-Z]{6}/\\d{1,3}.*$");

    private static final Pattern DEI_GENERIC =
            Pattern.compile("(^|\\s)/\\d{1,3}.*$");

    private static final Set<String> TIME_MODES = Set.of("UTC", "LT");

    /* ================= DETECTORS ================= */

    public static boolean isMessageReference(String line) {
        return MESSAGE_REF.matcher(line).matches();
    }

    public static boolean isPeriod(String line) {
        return PERIOD.matcher(line).matches();
    }

    /* ================= FLIGHT ================= */

    public static boolean isFlight(String line) {
        // Guard: must be single token (no spaces)
        if (line.contains(" ")) return false;

        return FLIGHT.matcher(line).matches() || isAsmFlightIdentifier(line);
    }

    /* ================= LEG ================= */

    public static boolean isLeg(String line) {

        // Guards (prevent overlap)
        if (line.contains("/")) return false;      // DEI
        if (isTime(line)) return false;            // TIME
        if (isFlight(line)) return false;          // FLIGHT

        return LEG.matcher(line).matches();
    }

    /* ================= TIME ================= */

    public static boolean isTime(String line) {
        return TIME.matcher(line).matches();
    }

    /* ================= EQUIPMENT ================= */

    public static boolean isEquipment(String line) {

        // Guard: avoid airport pair confusion
        if (line.matches("^[A-Z]{3}\\s+[A-Z]{3}$")) return false;

        // Guard: avoid misclassification
        if (isLeg(line)) return false;

        return EQUIPMENT.matcher(line).matches();
    }

    public static boolean isAsmFlightIdentifier(String line) {
        return line.matches("^[A-Z0-9]{2}\\d{1,4}[A-Z]?/\\d{2}([A-Z]{3})?(\\d{2})?$");
    }

    /* ================= DEI ================= */

    public static boolean isDei(String line) {

        // Strong signal
        if (!line.contains("/")) return false;

        return DEI_1.matcher(line).matches()
                || DEI_2.matcher(line).matches()
                || DEI_3.matcher(line).matches()
                || DEI_4.matcher(line).matches()
                || DEI_GENERIC.matcher(line).matches();
    }

    /* ================= HEADER / META ================= */

    public static boolean isTimeMode(String value) {
        return TIME_MODES.contains(value);
    }

    public static boolean isBlockEnd(String line) {
        return "//".equals(line);
    }

    public static String extractFirstToken(String line) {
        int idx = line.indexOf(' ');
        return idx > 0 ? line.substring(0, idx) : line;
    }

    public static boolean isDate(String line) {
        return line.matches("^\\d{2}[A-Z]{3}$");
    }

    public static boolean isHeaderTime(String line) {
        return line.matches("^\\d{4}$");
    }

    public static boolean isFlightWithRoute(String line) {
        return line.matches("^[A-Z0-9]{2}\\d{1,4}[A-Z]?\\s+[A-Z]{6}\\s+\\d{2}[A-Z]{3}(\\d{2})?$")
                || line.matches("^[A-Z0-9]{2}\\d{1,4}[A-Z]?/\\d{2}([A-Z]{3})?(\\d{2})?(\\s+[A-Z]{3}/[A-Z]{3}(/[A-Z]{3})*)?$");
    }

    public static boolean isLegWithTime(String line) {
        return line.matches("^[A-Z]{3}\\s+[A-Z]{3}\\s+\\d{4}(\\+\\d)?\\s+\\d{4}(\\+\\d)?$");
    }
}
