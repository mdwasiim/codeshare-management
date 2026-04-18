package com.codeshare.airline.inbound.orchestration.parsers.shared;

import java.time.LocalDate;
import java.time.Month;

public final class DateParser {

    private DateParser() {}

    public static LocalDate parse(String token) {

        int day = Integer.parseInt(token.substring(0, 2));
        String monthStr = token.substring(2, 5);

        Month month = parseMonth(monthStr);

        int year = LocalDate.now().getYear();

        LocalDate parsed = LocalDate.of(year, month, day);

        if (parsed.isBefore(LocalDate.now().minusMonths(6))) {
            parsed = parsed.plusYears(1);
        }

        return parsed;
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