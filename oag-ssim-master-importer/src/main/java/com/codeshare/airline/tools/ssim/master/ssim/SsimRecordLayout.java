package com.codeshare.airline.tools.ssim.master.ssim;

public final class SsimRecordLayout {
    public static final int RECORD_LENGTH = 200;

    public static final Slice T2_TIME_MODE = slice(1, 2);
    public static final Slice T2_AIRLINE = slice(2, 5);
    public static final Slice T2_SEASON = slice(10, 13);

    public static final Slice T3_OPERATIONAL_SUFFIX = slice(1, 2);
    public static final Slice T3_AIRLINE = slice(2, 5);
    public static final Slice T3_SERVICE_TYPE = slice(13, 14);
    public static final Slice T3_OPERATING_DAYS = slice(28, 35);
    public static final Slice T3_FREQUENCY = slice(35, 36);
    public static final Slice T3_DEPARTURE_STATION = slice(36, 39);
    public static final Slice T3_DEPARTURE_TERMINAL = slice(52, 54);
    public static final Slice T3_ARRIVAL_STATION = slice(54, 57);
    public static final Slice T3_ARRIVAL_TERMINAL = slice(70, 72);
    public static final Slice T3_AIRCRAFT_TYPE = slice(72, 75);
    public static final Slice T3_PRBD = slice(75, 95);
    public static final Slice T3_PRBM = slice(95, 100);
    public static final Slice T3_MEAL_SERVICE = slice(100, 110);
    public static final Slice T3_SECURE_FLIGHT = slice(121, 122);
    public static final Slice T3_AIRCRAFT_OWNER = slice(128, 131);
    public static final Slice T3_COCKPIT_EMPLOYER = slice(131, 134);
    public static final Slice T3_CABIN_EMPLOYER = slice(134, 137);
    public static final Slice T3_ONWARD_AIRLINE = slice(137, 140);
    public static final Slice T3_TRAFFIC_RESTRICTION = slice(149, 160);

    public static final Slice T4_AIRLINE = slice(2, 5);
    public static final Slice T4_DEI = slice(30, 33);
    public static final Slice T4_BOARD_POINT = slice(33, 36);
    public static final Slice T4_OFF_POINT = slice(36, 39);

    private SsimRecordLayout() {
    }

    public record Slice(int startInclusive, int endExclusive) {
        public String read(String line) {
            return line.substring(startInclusive, endExclusive);
        }
    }

    private static Slice slice(int startInclusive, int endExclusive) {
        return new Slice(startInclusive, endExclusive);
    }
}
