package com.codeshare.airline.schedule.parsing.common.dto;

import com.codeshare.airline.core.enums.schedule.DeiScopeLevel;
import lombok.Data;

@Data
public class InboundDei {

    private Integer deiCode;
    private String value;
    private DeiScopeLevel scope; // FLIGHT / LEG / SEGMENT
}