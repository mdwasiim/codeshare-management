package com.codeshare.airline.platform.core.dto.master.terminal;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TrafficConferenceAreaDTO {

    private Long id;
    private String areaCode;
    private String areaName;
    private String iataAreaCode;
    private String description;
    private Long regionId;
    private String regionCode;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
