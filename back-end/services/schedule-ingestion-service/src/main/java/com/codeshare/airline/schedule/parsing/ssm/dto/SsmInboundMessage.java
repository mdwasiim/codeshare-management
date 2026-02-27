package com.codeshare.airline.schedule.parsing.ssm.dto;

import com.codeshare.airline.schedule.parsing.common.dto.ActionIdentifier;
import com.codeshare.airline.schedule.parsing.common.dto.InboundDei;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class SsmInboundMessage {

    // --- Action ---
    private ActionIdentifier actionIdentifier;

    // --- Flight Identity ---
    private String carrier;
    private String flightNumber;
    private String suffix;

    // --- Date ---
    private LocalDate operationDate;

    // --- Routing ---
    private List<SsmInboundLeg> legs = new ArrayList<>();

    // --- Equipment ---
    private String aircraftType;

    // --- DEI ---
    private List<InboundDei> deis = new ArrayList<>();

    // --- Supplementary Info ---
    private List<String> supplementaryInfo = new ArrayList<>();
}