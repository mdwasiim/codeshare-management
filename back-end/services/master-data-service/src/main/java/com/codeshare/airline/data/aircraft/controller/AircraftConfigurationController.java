package com.codeshare.airline.data.aircraft.controller;

import com.codeshare.airline.core.dto.aircraft.AircraftConfigurationDTO;
import com.codeshare.airline.data.aircraft.service.AircraftConfigurationService;
import com.codeshare.airline.data.common.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/aircraft-configurations")
public class AircraftConfigurationController
        extends BaseController<AircraftConfigurationDTO, UUID> {

    public AircraftConfigurationController(
            AircraftConfigurationService service) {
        super(service);
    }
}