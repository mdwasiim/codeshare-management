package com.codeshare.airline.inbound.dto.common.base;

import lombok.Data;

@Data
public class ScheduleIdentityDTO {

    private String airlineDesignator;
    private String flightNumber;
    private String operationalSuffix;

    // 🔥 ADD THESE (ASM support)
    private String boardPoint;
    private String offPoint;
    private String operationDate;

    // =========================
    // HELPERS
    // =========================

    public String getFullFlightNumber() {
        return (airlineDesignator != null ? airlineDesignator : "") +
                (flightNumber != null ? flightNumber : "") +
                (operationalSuffix != null ? operationalSuffix : "");
    }

    public boolean isValid() {
        return airlineDesignator != null && airlineDesignator.length() >= 2 &&
                flightNumber != null && !flightNumber.isBlank();
    }

    public boolean hasRoute() {
        return boardPoint != null && offPoint != null;
    }

    public boolean hasDate() {
        return operationDate != null;
    }
}