package com.codeshare.airline.schedule.ingestion.orchestration.parsers.shared;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TimeParser {

    private TimeParser() {}

    // 0900 / 0900+1
    private static final Pattern TIME_PATTERN =
            Pattern.compile("^(\\d{4})(\\+(\\d))?$");

    public static ParsedTime parse(String line) {

        if (line == null || line.isBlank()) return null;

        String[] parts = line.trim().split("\\s+");

        if (parts.length < 2) return null;

        try {
            TimeWithOffset dep = parseSingle(parts[0]);
            TimeWithOffset arr = parseSingle(parts[1]);

            return new ParsedTime(dep.time, arr.time, dep.offset, arr.offset);

        } catch (Exception e) {
            return null; // 🔥 NEVER throw
        }
    }

    private static TimeWithOffset parseSingle(String value) {

        Matcher matcher = TIME_PATTERN.matcher(value);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid time: " + value);
        }

        String hhmm = matcher.group(1);
        String offsetStr = matcher.group(3);

        int hour = Integer.parseInt(hhmm.substring(0, 2));
        int min  = Integer.parseInt(hhmm.substring(2, 4));

        LocalTime time = LocalTime.of(hour, min);

        int offset = offsetStr != null ? Integer.parseInt(offsetStr) : 0;

        return new TimeWithOffset(time, offset);
    }

    /* ================= DTO ================= */

    public static class ParsedTime {
        private final LocalTime std;
        private final LocalTime sta;
        private final int depOffset;
        private final int arrOffset;

        public ParsedTime(LocalTime std, LocalTime sta, int depOffset, int arrOffset) {
            this.std = std;
            this.sta = sta;
            this.depOffset = depOffset;
            this.arrOffset = arrOffset;
        }

        public LocalTime getStd() { return std; }
        public LocalTime getSta() { return sta; }
        public int getDepOffset() { return depOffset; }
        public int getArrOffset() { return arrOffset; }
    }

    private static class TimeWithOffset {
        LocalTime time;
        int offset;

        TimeWithOffset(LocalTime time, int offset) {
            this.time = time;
            this.offset = offset;
        }
    }
}