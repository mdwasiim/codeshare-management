package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.platform.core.dto.master.aircraft.AirlineFleetDTO;
import com.codeshare.airline.master.aircraft.service.AirlineFleetService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/airline-fleet")
public class AirlineFleetController
        extends BaseController<AirlineFleetDTO, Long> {

    private final AirlineFleetService service;

    public AirlineFleetController(AirlineFleetService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/airline/{airlineId}")
    public List<AirlineFleetDTO> getByAirline(
            @PathVariable Long airlineId) {
        return service.getByAirline(airlineId);
    }

    @GetMapping("/configuration/{configId}")
    public List<AirlineFleetDTO> getByConfiguration(
            @PathVariable Long configId) {
        return service.getByConfiguration(configId);
    }
}