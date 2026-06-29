package com.codeshare.airline.inbound.entities.converter;

import com.codeshare.airline.inbound.domain.enums.TimeMode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TimeModeCodeConverter implements AttributeConverter<TimeMode, String> {

    @Override
    public String convertToDatabaseColumn(TimeMode attribute) {
        if (attribute == null) {
            return null;
        }
        return switch (attribute) {
            case LT -> "L";
            case UTC -> "U";
        };
    }

    @Override
    public TimeMode convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return switch (dbData.trim()) {
            case "L", "LT" -> TimeMode.LT;
            case "U", "UTC" -> TimeMode.UTC;
            default -> throw new IllegalArgumentException("Invalid TimeMode code: " + dbData);
        };
    }
}
