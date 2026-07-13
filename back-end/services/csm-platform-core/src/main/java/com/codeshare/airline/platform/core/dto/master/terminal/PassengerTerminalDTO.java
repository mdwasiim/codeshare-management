package com.codeshare.airline.platform.core.dto.master.terminal;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PassengerTerminalDTO {

    private Long id;
    private String airportCode;
    private String terminalCode;
    private String terminalName;
    private String terminalType;
    private Boolean internationalFlag;
    private String status;
}