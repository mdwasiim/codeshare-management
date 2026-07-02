package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.core.dto.master.aircraft.CabinCrewOperatorDTO;
import com.codeshare.airline.master.aircraft.service.CabinCrewOperatorService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/cabin-crew-operators")
public class CabinCrewOperatorController
        extends BaseController<CabinCrewOperatorDTO, UUID> {

    public CabinCrewOperatorController(CabinCrewOperatorService service) {
        super(service);
    }
}
