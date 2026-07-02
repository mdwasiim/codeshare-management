package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.core.dto.master.aircraft.AircraftConfigurationRevisionDTO;
import com.codeshare.airline.master.aircraft.service.AircraftConfigurationRevisionService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/aircraft-configuration-revisions")
public class AircraftConfigurationRevisionController
        extends BaseController<AircraftConfigurationRevisionDTO, UUID> {

    private final AircraftConfigurationRevisionService service;

    public AircraftConfigurationRevisionController(AircraftConfigurationRevisionService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/configuration/{aircraftConfigurationId}")
    public List<AircraftConfigurationRevisionDTO> getByAircraftConfiguration(
            @PathVariable UUID aircraftConfigurationId) {
        return service.getByAircraftConfiguration(aircraftConfigurationId);
    }
}
