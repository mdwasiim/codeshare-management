package com.codeshare.airline.inbound.orchestration.parsers.shared;

import com.codeshare.airline.inbound.dto.common.base.ScheduleEquipmentDTO;

public final class EquipmentParser {

    private EquipmentParser() {}

    public static ScheduleEquipmentDTO parse(String line) {

        if (line == null || line.isBlank()) return null;

        String[] parts = line.trim().split("\\s+");

        if (parts.length == 0) return null;

        String aircraft = parts[0];

        // Reject airport codes (3-letter)
        if (!aircraft.matches("^[A-Z0-9]{2,4}$") || aircraft.matches("^[A-Z]{3}$")) {
            return null;
        }

        ScheduleEquipmentDTO equipment = new ScheduleEquipmentDTO();
        equipment.setAircraftType(aircraft);

        if (parts.length > 1) {
            // join remaining tokens safely
            String service = String.join("", java.util.Arrays.copyOfRange(parts, 1, parts.length));
            equipment.setServiceType(service);
        }

        return equipment;
    }
}