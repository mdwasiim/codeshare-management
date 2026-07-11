package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.platform.core.dto.master.aircraft.CockpitCrewOperatorDTO;
import com.codeshare.airline.master.aircraft.service.CockpitCrewOperatorService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/cockpit-crew-operators")
public class CockpitCrewOperatorController
        extends BaseController<CockpitCrewOperatorDTO, UUID> {

    public CockpitCrewOperatorController(CockpitCrewOperatorService service) {
        super(service);
    }
}
