package com.codeshare.airline.schedule.ingestion.orchestration.parsers.shared;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TimeParser {

    private TimeParser() {}

    // 0900 / 0900+1 / 301900 / 301900+1
    private static final Pattern TIME_PATTERN =
            Pattern.compile("^((\\d{2})?(\\d{4}))(\\+(\\d))?$");

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

    public static ParsedSingleTime parseToken(String value) {
        TimeWithOffset parsed = parseSingle(value);
        return new ParsedSingleTime(parsed.time, parsed.offset);
    }

    private static TimeWithOffset parseSingle(String value) {

        Matcher matcher = TIME_PATTERN.matcher(value);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid time: " + value);
        }

        String hhmmWithOptionalDay = matcher.group(1);
        String hhmm = hhmmWithOptionalDay.length() == 6
                ? hhmmWithOptionalDay.substring(2)
                : hhmmWithOptionalDay;
        String offsetStr = matcher.group(5);

        int hour = Integer.parseInt(hhmm.substring(0, 2));
        int min  = Integer.parseInt(hhmm.substring(2, 4));

        int offset = offsetStr != null ? Integer.parseInt(offsetStr) : 0;
        LocalTime time;

        if (hour == 24 && min == 0) {
            time = LocalTime.MIDNIGHT;
            offset += 1;
        } else {
            time = LocalTime.of(hour, min);
        }

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

    public static class ParsedSingleTime {
        private final LocalTime time;
        private final int offset;

        public ParsedSingleTime(LocalTime time, int offset) {
            this.time = time;
            this.offset = offset;
        }

        public LocalTime getTime() {
            return time;
        }

        public int getOffset() {
            return offset;
        }
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
