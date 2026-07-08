package com.codeshare.airline.schedule.ingestion.orchestration.parsers.shared;

import java.time.LocalDate;
import java.time.Month;

public final class DateParser {

    private static final int TWO_DIGIT_YEAR_PIVOT = 70;

    private DateParser() {}

    public static LocalDate parse(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Date token is blank");
        }

        String normalized = token.trim().toUpperCase();
        if (!normalized.matches("\\d{2}[A-Z]{3}(\\d{2})?")) {
            throw new IllegalArgumentException("Invalid date token: " + token);
        }

        int day = Integer.parseInt(normalized.substring(0, 2));
        String monthStr = normalized.substring(2, 5);

        Month month = parseMonth(monthStr);

        int year = normalized.length() == 7
                ? resolveTwoDigitYear(Integer.parseInt(normalized.substring(5, 7)))
                : LocalDate.now().getYear();

        LocalDate parsed = LocalDate.of(year, month, day);

        if (normalized.length() == 5 && parsed.isBefore(LocalDate.now().minusMonths(6))) {
            parsed = parsed.plusYears(1);
        }

        return parsed;
    }

    private static int resolveTwoDigitYear(int twoDigitYear) {
        return twoDigitYear >= TWO_DIGIT_YEAR_PIVOT ? 1900 + twoDigitYear : 2000 + twoDigitYear;
    }

    private static Month parseMonth(String m) {
        return switch (m) {
            case "JAN" -> Month.JANUARY;
            case "FEB" -> Month.FEBRUARY;
            case "MAR" -> Month.MARCH;
            case "APR" -> Month.APRIL;
            case "MAY" -> Month.MAY;
            case "JUN" -> Month.JUNE;
            case "JUL" -> Month.JULY;
            case "AUG" -> Month.AUGUST;
            case "SEP" -> Month.SEPTEMBER;
            case "OCT" -> Month.OCTOBER;
            case "NOV" -> Month.NOVEMBER;
            case "DEC" -> Month.DECEMBER;
            default -> throw new IllegalArgumentException("Invalid month: " + m);
        };
    }
}
