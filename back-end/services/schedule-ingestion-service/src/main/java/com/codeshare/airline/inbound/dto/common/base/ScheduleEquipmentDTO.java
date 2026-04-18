package com.codeshare.airline.inbound.dto.common.base;

import lombok.Data;

@Data
public class ScheduleEquipmentDTO {

    /* ================= SERVICE ================= */

    private String serviceType;

    /* ================= AIRCRAFT ================= */

    private String aircraftType;

    /* ================= BOOKING ================= */

    private String bookingDesignator;

    /* ================= CONFIGURATION ================= */

    private String aircraftConfiguration;

    // =========================
    // HELPERS
    // =========================

    public boolean hasAircraftType() {
        return aircraftType != null && !aircraftType.isBlank();
    }

    public Integer getCapacity() {
        if (aircraftConfiguration == null) return null;

        try {
            return aircraftConfiguration.chars()
                    .map(Character::getNumericValue)
                    .sum();
        } catch (Exception e) {
            return null;
        }
    }
}