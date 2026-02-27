package com.codeshare.airline.schedule.parsing.asm.dto;

import com.codeshare.airline.schedule.parsing.common.dto.ActionIdentifier;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AsmFlightBlock {

    private ActionIdentifier actionIdentifier;
    private String flightLine;
    private String periodLine;
    private String daysLine;
    private String routingLine;

    private List<String> equipmentLines = new ArrayList<>();
    private List<String> deiLines = new ArrayList<>();
    private List<String> supplementaryLines = new ArrayList<>();

}