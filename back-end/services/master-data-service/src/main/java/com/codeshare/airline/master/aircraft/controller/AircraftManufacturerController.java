package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftManufacturerDTO;
import com.codeshare.airline.master.aircraft.service.AircraftManufacturerService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/aircraft-manufacturers")
public class AircraftManufacturerController
        extends BaseController<AircraftManufacturerDTO, UUID> {

    public AircraftManufacturerController(AircraftManufacturerService service) {
        super(service);
    }
}
