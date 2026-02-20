package com.codeshare.airline.data.aircraft.controller;

import com.codeshare.airline.core.dto.aircraft.AircraftCabinLayoutDTO;
import com.codeshare.airline.data.aircraft.service.AircraftCabinLayoutService;
import com.codeshare.airline.data.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/aircraft-cabin-layouts")
public class AircraftCabinLayoutController
        extends BaseController<AircraftCabinLayoutDTO, UUID> {

    private final AircraftCabinLayoutService service;
    public AircraftCabinLayoutController(
            AircraftCabinLayoutService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/configuration/{configId}")
    public List<AircraftCabinLayoutDTO> getByConfiguration(
            @PathVariable UUID configId) {
        return service.getByConfiguration(configId);
    }
}