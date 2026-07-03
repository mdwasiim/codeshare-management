package com.codeshare.airline.schedule.ingestion.persistence.mappers.ssim;

import com.codeshare.airline.schedule.ingestion.domain.enums.RecordType;

final class SsimRecordTypeMapper {

    private SsimRecordTypeMapper() {
    }

    static String toCode(RecordType recordType) {
        return recordType == null ? null : String.valueOf(recordType.getCode());
    }

    static RecordType fromCode(String recordType) {
        if (recordType == null || recordType.isBlank()) {
            return null;
        }
        String value = recordType.trim();
        if (value.length() == 1 && Character.isDigit(value.charAt(0))) {
            return RecordType.fromCode(Integer.parseInt(value));
        }
        if (value.length() == 2 && value.charAt(0) == 'T' && Character.isDigit(value.charAt(1))) {
            return RecordType.fromCode(Character.getNumericValue(value.charAt(1)));
        }
        return RecordType.valueOf(value);
    }
}
