package com.codeshare.airline.data.airport.terminal.service;


import com.codeshare.airline.core.dto.airport.terminal.PassengerTerminalDTO;
import com.codeshare.airline.persistence.persistence.service.BaseService;

import java.util.List;
import java.util.UUID;

public interface PassengerTerminalService
        extends BaseService<PassengerTerminalDTO, UUID> {

    List<PassengerTerminalDTO> getByAirport(UUID airportId);
}