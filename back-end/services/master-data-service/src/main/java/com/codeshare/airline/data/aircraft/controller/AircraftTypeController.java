package com.codeshare.airline.data.aircraft.controller;

import com.codeshare.airline.core.dto.aircraft.AircraftTypeDTO;
import com.codeshare.airline.data.aircraft.service.AircraftTypeService;
import com.codeshare.airline.data.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/aircraft-types")
public class AircraftTypeController
        extends BaseController<AircraftTypeDTO, UUID> {

    private final AircraftTypeService service;

    public AircraftTypeController(AircraftTypeService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/icao/{icao}")
    public AircraftTypeDTO getByIcao(@PathVariable String icao) {
        return service.getByIcao(icao);
    }

    @GetMapping("/model/{model}")
    public AircraftTypeDTO getByModel(@PathVariable String model) {
        return service.getByModel(model);
    }
}