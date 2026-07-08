package com.codeshare.airline.schedule.ingestion.orchestration.parsers.shared;

import com.codeshare.airline.schedule.ingestion.dto.schedule.SchedulePeriodDTO;

public final class PeriodParser {

    private PeriodParser() {} // 🔥 prevent instantiation

    public static SchedulePeriodDTO parse(String line) {

        SchedulePeriodDTO period = new SchedulePeriodDTO();

        if (line == null || line.isBlank()) {
            return period;
        }

        String[] tokens = line.trim().toUpperCase().split("\\s+");

        int index = 0;

        /* ================= FROM DATE ================= */

        if (index < tokens.length && isDate(tokens[index])) {
            period.setStartDate(DateParser.parse(tokens[index]));
            index++;
        }

        /* ================= TO DATE ================= */

        if (index < tokens.length && isDate(tokens[index])) {
            period.setEndDate(DateParser.parse(tokens[index]));
            index++;
        }

        /* ================= DAYS / FREQUENCY ================= */

        if (index < tokens.length) {

            String dayToken = tokens[index];

            if (dayToken.contains("/")) {

                String[] parts = dayToken.split("/");

                if (parts.length == 2) {
                    period.setDaysOfOperation(parts[0]);
                    String frequencyToken = parts[1].trim().toUpperCase();
                    if (frequencyToken.startsWith("W")) {
                        frequencyToken = frequencyToken.substring(1);
                    }
                    period.setFrequencyRate(parseInteger(frequencyToken));
                }

            } else {
                period.setDaysOfOperation(dayToken);
            }
        }

        return period;
    }

    /* ================= HELPERS ================= */

    private static boolean isDate(String token) {
        return token != null && token.matches("\\d{2}[A-Z]{3}(\\d{2})?");
    }

    private static Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }
}
