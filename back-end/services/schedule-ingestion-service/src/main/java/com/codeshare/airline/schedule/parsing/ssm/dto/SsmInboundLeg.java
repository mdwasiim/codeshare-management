package com.codeshare.airline.schedule.parsing.ssm.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class SsmInboundLeg {

    private String origin;
    private String destination;

    private LocalTime departureTime;
    private LocalTime arrivalTime;

    private String aircraftType;
}