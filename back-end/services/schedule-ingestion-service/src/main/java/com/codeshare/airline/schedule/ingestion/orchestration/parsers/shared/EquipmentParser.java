package com.codeshare.airline.schedule.ingestion.orchestration.parsers.shared;

import com.codeshare.airline.schedule.ingestion.dto.common.base.ScheduleEquipmentDTO;

public final class EquipmentParser {

    private EquipmentParser() {}

    public static ScheduleEquipmentDTO parse(String line) {

        if (line == null || line.isBlank()) return null;

        String[] parts = line.trim().split("\\s+");

        if (parts.length < 2) return null;

        String serviceType = parts[0];
        String aircraft = parts[1];

        if (!serviceType.matches("^[A-Z]$") || !aircraft.matches("^[A-Z0-9]{2,4}$")) {
            return null;
        }

        ScheduleEquipmentDTO equipment = new ScheduleEquipmentDTO();
        equipment.setServiceType(serviceType);
        equipment.setAircraftType(aircraft);

        for (int i = 2; i < parts.length; i++) {
            String token = parts[i];
            if (token.startsWith(".")) {
                equipment.setAircraftConfiguration(token.substring(1));
            } else if (token.startsWith("/")) {
                equipment.setBookingDesignator(token);
            } else if (equipment.getBookingDesignator() == null && token.matches("^[A-Z0-9]{1,20}$")) {
                equipment.setBookingDesignator(token);
            }
        }

        return equipment;
    }
}
