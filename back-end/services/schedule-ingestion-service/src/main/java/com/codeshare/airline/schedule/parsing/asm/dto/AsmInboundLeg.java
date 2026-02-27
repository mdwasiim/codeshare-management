package com.codeshare.airline.schedule.parsing.asm.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class AsmInboundLeg {

    // --- Airports ---
    private String origin;          // e.g. DOH
    private String destination;     // e.g. LHR

    // --- Times (Local unless otherwise specified) ---
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    // --- Optional ---
    private String aircraftType;    // If equipment varies per leg (rare but possible)
}
