package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftCabinLayoutDTO;
import com.codeshare.airline.master.aircraft.service.AircraftCabinLayoutService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/aircraft-cabin-layouts")
public class AircraftCabinLayoutController
        extends BaseController<AircraftCabinLayoutDTO, Long> {

    private final AircraftCabinLayoutService service;
    public AircraftCabinLayoutController(
            AircraftCabinLayoutService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/configuration/{configId}")
    public List<AircraftCabinLayoutDTO> getByConfiguration(
            @PathVariable Long configId) {
        return service.getByConfiguration(configId);
    }
}