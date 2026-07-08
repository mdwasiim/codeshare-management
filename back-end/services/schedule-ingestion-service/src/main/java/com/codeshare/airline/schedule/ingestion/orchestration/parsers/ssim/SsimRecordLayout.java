package com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim;

public final class SsimRecordLayout {

    public static final int RECORD_LENGTH = 200;

    private SsimRecordLayout() {
    }

    public static final Slice T1_TITLE = slice(1, 35);
    public static final Slice T1_SPARE_36_40 = slice(35, 40);
    public static final Slice T1_NUMBER_OF_SEASONS = slice(40, 41);
    public static final Slice T1_SPARE_42_191 = slice(41, 191);
    public static final Slice T1_DATASET_SERIAL = slice(191, 194);
    public static final Slice T1_RECORD_SERIAL = slice(194, 200);

    public static final Slice T2_TIME_MODE = slice(1, 2);
    public static final Slice T2_AIRLINE = slice(2, 5);
    public static final Slice T2_SPARE_6_10 = slice(5, 10);
    public static final Slice T2_SEASON = slice(10, 13);
    public static final Slice T2_SPARE_14 = slice(13, 14);
    public static final Slice T2_VALIDITY_START = slice(14, 21);
    public static final Slice T2_VALIDITY_END = slice(21, 28);
    public static final Slice T2_CREATION_DATE = slice(28, 35);
    public static final Slice T2_TITLE = slice(35, 64);
    public static final Slice T2_RELEASE_DATE = slice(64, 71);
    public static final Slice T2_STATUS = slice(71, 72);
    public static final Slice T2_CREATOR_REFERENCE = slice(72, 107);
    public static final Slice T2_DUPLICATE_MARKER = slice(107, 108);
    public static final Slice T2_GENERAL_INFO = slice(108, 169);
    public static final Slice T2_INFLIGHT_SERVICE = slice(169, 188);
    public static final Slice T2_ET_INFO = slice(188, 190);
    public static final Slice T2_CREATION_TIME = slice(190, 194);
    public static final Slice T2_RECORD_SERIAL = slice(194, 200);

    public static final Slice T3_OPERATIONAL_SUFFIX = slice(1, 2);
    public static final Slice T3_AIRLINE = slice(2, 5);
    public static final Slice T3_FLIGHT_NUMBER = slice(5, 9);
    public static final Slice T3_IVI = slice(9, 11);
    public static final Slice T3_LEG_SEQUENCE = slice(11, 13);
    public static final Slice T3_SERVICE_TYPE = slice(13, 14);
    public static final Slice T3_PERIOD_START = slice(14, 21);
    public static final Slice T3_PERIOD_END = slice(21, 28);
    public static final Slice T3_OPERATING_DAYS = slice(28, 35);
    public static final Slice T3_FREQUENCY = slice(35, 36);
    public static final Slice T3_DEPARTURE_STATION = slice(36, 39);
    public static final Slice T3_PASSENGER_STD = slice(39, 43);
    public static final Slice T3_AIRCRAFT_STD = slice(43, 47);
    public static final Slice T3_DEPARTURE_UTC_VARIATION = slice(47, 52);
    public static final Slice T3_DEPARTURE_TERMINAL = slice(52, 54);
    public static final Slice T3_ARRIVAL_STATION = slice(54, 57);
    public static final Slice T3_AIRCRAFT_STA = slice(57, 61);
    public static final Slice T3_PASSENGER_STA = slice(61, 65);
    public static final Slice T3_ARRIVAL_UTC_VARIATION = slice(65, 70);
    public static final Slice T3_ARRIVAL_TERMINAL = slice(70, 72);
    public static final Slice T3_AIRCRAFT_TYPE = slice(72, 75);
    public static final Slice T3_PRBD = slice(75, 95);
    public static final Slice T3_PRBM = slice(95, 100);
    public static final Slice T3_MEAL_SERVICE = slice(100, 110);
    public static final Slice T3_JOINT_OPERATION = slice(110, 119);
    public static final Slice T3_MCT_STATUS = slice(119, 121);
    public static final Slice T3_SECURE_FLIGHT = slice(121, 122);
    public static final Slice T3_SPARE_123_127 = slice(122, 127);
    public static final Slice T3_IVI_OVERFLOW = slice(127, 128);
    public static final Slice T3_AIRCRAFT_OWNER = slice(128, 131);
    public static final Slice T3_COCKPIT_EMPLOYER = slice(131, 134);
    public static final Slice T3_CABIN_EMPLOYER = slice(134, 137);
    public static final Slice T3_ONWARD_AIRLINE = slice(137, 140);
    public static final Slice T3_ONWARD_FLIGHT_NUMBER = slice(140, 144);
    public static final Slice T3_AIRCRAFT_ROTATION_LAYOVER = slice(144, 145);
    public static final Slice T3_ONWARD_OPERATIONAL_SUFFIX = slice(145, 146);
    public static final Slice T3_SPARE_147 = slice(146, 147);
    public static final Slice T3_FLIGHT_TRANSIT_LAYOVER = slice(147, 148);
    public static final Slice T3_DISCLOSURE = slice(148, 149);
    public static final Slice T3_TRAFFIC_RESTRICTION = slice(149, 160);
    public static final Slice T3_TRAFFIC_RESTRICTION_OVERFLOW = slice(160, 161);
    public static final Slice T3_SPARE_162_172 = slice(161, 172);
    public static final Slice T3_ACV = slice(172, 192);
    public static final Slice T3_DATE_VARIATION = slice(192, 194);
    public static final Slice T3_RECORD_SERIAL = slice(194, 200);

    public static final Slice T4_OPERATIONAL_SUFFIX = slice(1, 2);
    public static final Slice T4_AIRLINE = slice(2, 5);
    public static final Slice T4_FLIGHT_NUMBER = slice(5, 9);
    public static final Slice T4_IVI = slice(9, 11);
    public static final Slice T4_LEG_SEQUENCE = slice(11, 13);
    public static final Slice T4_SERVICE_TYPE = slice(13, 14);
    public static final Slice T4_SPARE_15_27 = slice(14, 27);
    public static final Slice T4_IVI_OVERFLOW = slice(27, 28);
    public static final Slice T4_BOARD_INDICATOR = slice(28, 29);
    public static final Slice T4_OFF_INDICATOR = slice(29, 30);
    public static final Slice T4_DEI = slice(30, 33);
    public static final Slice T4_BOARD_POINT = slice(33, 36);
    public static final Slice T4_OFF_POINT = slice(36, 39);
    public static final Slice T4_DATA = slice(39, 194);
    public static final Slice T4_RECORD_SERIAL = slice(194, 200);

    public static final Slice T5_SPARE_2 = slice(1, 2);
    public static final Slice T5_AIRLINE = slice(2, 5);
    public static final Slice T5_RELEASE_DATE = slice(5, 12);
    public static final Slice T5_SPARE_13_187 = slice(12, 187);
    public static final Slice T5_SERIAL_CHECK_REFERENCE = slice(187, 193);
    public static final Slice T5_CONTINUATION_END = slice(193, 194);
    public static final Slice T5_RECORD_SERIAL = slice(194, 200);

    public record Slice(int startInclusive, int endExclusive) {
        public String read(String line) {
            return line.substring(startInclusive, endExclusive);
        }
    }

    private static Slice slice(int startInclusive, int endExclusive) {
        return new Slice(startInclusive, endExclusive);
    }
}
