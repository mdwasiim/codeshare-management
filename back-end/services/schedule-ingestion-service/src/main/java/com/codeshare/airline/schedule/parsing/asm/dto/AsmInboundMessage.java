package com.codeshare.airline.schedule.parsing.asm.dto;

import com.codeshare.airline.schedule.parsing.common.dto.ActionIdentifier;
import com.codeshare.airline.schedule.parsing.common.dto.InboundDei;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class AsmInboundMessage {

    // --- Action ---
    private ActionIdentifier actionIdentifier;

    // --- Flight Identity ---
    private String carrier;            // e.g. QR
    private String flightNumber;       // e.g. 123
    private String suffix;             // e.g. A (optional)

    // --- Period of Operation (Chapter 4 specific) ---
    private LocalDate periodFrom;      // 15DEC
    private LocalDate periodTo;        // 31MAR

    // --- Days of Operation ---
    // Format: 1234567
    private String daysOfOperation;

    // --- Routing ---
    private List<AsmInboundLeg> legs = new ArrayList<>();

    // --- Equipment ---
    private String aircraftType;   // optional (EQT line)

    // --- DEI ---
    private List<InboundDei> deis = new ArrayList<>();

    // --- Supplementary Info (optional but recommended) ---
    private List<String> supplementaryInfo = new ArrayList<>();

}