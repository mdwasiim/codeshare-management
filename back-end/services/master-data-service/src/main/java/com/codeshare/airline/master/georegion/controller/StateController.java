package com.codeshare.airline.master.georegion.controller;

import com.codeshare.airline.core.dto.airport.georegion.StateDTO;
import com.codeshare.airline.master.georegion.service.StateService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/states")
public class StateController
        extends BaseController<StateDTO, UUID> {

    public StateController(StateService service) {
        super(service);
    }
}