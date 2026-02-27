package com.codeshare.airline.schedule.parsing.ssm.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SsmFlightBlock {

    private String actionIdentifier;
    private String flightDesignatorLine;
    private String routingLine;

    private List<String> equipmentLines = new ArrayList<>();
    private List<String> deiLines = new ArrayList<>();
    private List<String> supplementaryLines = new ArrayList<>();

}