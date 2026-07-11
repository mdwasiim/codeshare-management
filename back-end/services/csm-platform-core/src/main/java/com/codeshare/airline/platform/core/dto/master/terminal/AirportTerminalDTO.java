package com.codeshare.airline.platform.core.dto.master.terminal;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AirportTerminalDTO {

    private UUID id;
    private String terminalCode;
    private String terminalName;
    private String iataTerminalCode;
    private String description;
    private UUID airportId;
    private String airportCode;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
