package com.codeshare.airline.schedule.ingestion.domain.enums;

public enum RecordType {

    HEADER(1),
    CARRIER(2),
    LEG(3),
    DEI(4),
    TRAILER(5);

    private final int code;

    RecordType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    // 🔥 THIS is what you are asking
    public static RecordType fromCode(int code) {
        for (RecordType type : RecordType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid RecordType code: " + code);
    }
}