package com.codeshare.airline.core.dto.master.terminal;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class TrafficConferenceAreaDTO {

    private UUID id;
    private String areaCode;
    private String areaName;
    private String iataAreaCode;
    private String description;
    private UUID regionId;
    private String regionCode;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
