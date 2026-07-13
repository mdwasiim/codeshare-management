package com.codeshare.airline.master.terminal.service;


import com.codeshare.airline.platform.core.dto.master.terminal.PassengerTerminalDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.List;

public interface PassengerTerminalService
        extends BaseService<PassengerTerminalDTO, Long> {

    List<PassengerTerminalDTO> getByAirport(Long airportId);
}