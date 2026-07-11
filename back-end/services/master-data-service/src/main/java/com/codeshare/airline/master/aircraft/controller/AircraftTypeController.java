package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftTypeDTO;
import com.codeshare.airline.master.aircraft.service.AircraftTypeService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/aircraft-types")
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