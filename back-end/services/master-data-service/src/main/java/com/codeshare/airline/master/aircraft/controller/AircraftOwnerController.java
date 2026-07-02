package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.core.dto.aircraft.AircraftOwnerDTO;
import com.codeshare.airline.master.aircraft.service.AircraftOwnerService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/aircraft-owners")
public class AircraftOwnerController
        extends BaseController<AircraftOwnerDTO, UUID> {

    public AircraftOwnerController(AircraftOwnerService service) {
        super(service);
    }
}
