package com.codeshare.airline.master.terminal.service;


import com.codeshare.airline.core.dto.airport.terminal.PassengerTerminalDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface PassengerTerminalService
        extends BaseService<PassengerTerminalDTO, UUID> {

    List<PassengerTerminalDTO> getByAirport(UUID airportId);
}