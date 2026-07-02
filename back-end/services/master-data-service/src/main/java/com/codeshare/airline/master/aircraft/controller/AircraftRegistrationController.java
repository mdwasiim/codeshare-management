package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.core.dto.aircraft.AircraftRegistrationDTO;
import com.codeshare.airline.master.aircraft.service.AircraftRegistrationService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/aircraft-registrations")
public class AircraftRegistrationController
        extends BaseController<AircraftRegistrationDTO, UUID> {

    public AircraftRegistrationController(AircraftRegistrationService service) {
        super(service);
    }
}
