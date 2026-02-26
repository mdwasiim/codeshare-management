package com.codeshare.airline.core.dto.airport.terminal;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PassengerTerminalDTO {

    private UUID id;
    private String airportCode;
    private String terminalCode;
    private String terminalName;
    private String terminalType;
    private Boolean internationalFlag;
    private String status;
}