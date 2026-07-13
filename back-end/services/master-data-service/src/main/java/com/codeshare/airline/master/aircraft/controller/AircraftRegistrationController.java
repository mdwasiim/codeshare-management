package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftRegistrationDTO;
import com.codeshare.airline.master.aircraft.service.AircraftRegistrationService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/aircraft-registrations")
public class AircraftRegistrationController
        extends BaseController<AircraftRegistrationDTO, Long> {

    public AircraftRegistrationController(AircraftRegistrationService service) {
        super(service);
    }
}
