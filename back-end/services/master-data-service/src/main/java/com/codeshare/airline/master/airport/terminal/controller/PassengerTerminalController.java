package com.codeshare.airline.master.airport.terminal.controller;

import com.codeshare.airline.core.dto.airport.terminal.PassengerTerminalDTO;
import com.codeshare.airline.master.airport.terminal.service.PassengerTerminalService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/airport-terminals")
public class PassengerTerminalController
        extends BaseController<PassengerTerminalDTO, UUID> {

    private final PassengerTerminalService service;

    public PassengerTerminalController(
            PassengerTerminalService service) {

        super(service);
        this.service = service;
    }

    /**
     * Custom: Get terminals by Airport
     */
    @GetMapping("/airport/{airportId}")
    public List<PassengerTerminalDTO> getByAirport(
            @PathVariable UUID airportId) {

        return service.getByAirport(airportId);
    }
}