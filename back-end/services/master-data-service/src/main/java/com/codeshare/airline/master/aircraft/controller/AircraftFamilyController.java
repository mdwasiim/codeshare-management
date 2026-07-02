package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.core.dto.aircraft.AircraftFamilyDTO;
import com.codeshare.airline.master.aircraft.service.AircraftFamilyService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/aircraft-families")
public class AircraftFamilyController
        extends BaseController<AircraftFamilyDTO, UUID> {

    public AircraftFamilyController(AircraftFamilyService service) {
        super(service);
    }
}
