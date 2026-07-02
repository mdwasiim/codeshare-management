package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.core.dto.master.aircraft.AircraftConfigurationDTO;
import com.codeshare.airline.master.aircraft.service.AircraftConfigurationService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/aircraft-configurations")
public class AircraftConfigurationController
        extends BaseController<AircraftConfigurationDTO, UUID> {

    public AircraftConfigurationController(
            AircraftConfigurationService service) {
        super(service);
    }
}