package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftOwnerDTO;
import com.codeshare.airline.master.aircraft.service.AircraftOwnerService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/aircraft-owners")
public class AircraftOwnerController
        extends BaseController<AircraftOwnerDTO, Long> {

    public AircraftOwnerController(AircraftOwnerService service) {
        super(service);
    }
}
