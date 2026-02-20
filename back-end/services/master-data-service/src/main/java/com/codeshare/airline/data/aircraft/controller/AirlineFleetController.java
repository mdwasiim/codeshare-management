package com.codeshare.airline.data.aircraft.controller;

import com.codeshare.airline.core.dto.aircraft.AirlineFleetDTO;
import com.codeshare.airline.data.aircraft.service.AirlineFleetService;
import com.codeshare.airline.data.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/airline-fleet")
public class AirlineFleetController
        extends BaseController<AirlineFleetDTO, UUID> {

    private final AirlineFleetService service;

    public AirlineFleetController(AirlineFleetService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/airline/{airlineId}")
    public List<AirlineFleetDTO> getByAirline(
            @PathVariable UUID airlineId) {
        return service.getByAirline(airlineId);
    }

    @GetMapping("/configuration/{configId}")
    public List<AirlineFleetDTO> getByConfiguration(
            @PathVariable UUID configId) {
        return service.getByConfiguration(configId);
    }
}