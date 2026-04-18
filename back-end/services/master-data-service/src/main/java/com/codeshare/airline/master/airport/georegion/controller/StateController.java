package com.codeshare.airline.master.airport.georegion.controller;

import com.codeshare.airline.dto.airport.georegion.StateDTO;
import com.codeshare.airline.master.airport.georegion.service.StateService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/states")
public class StateController
        extends BaseController<StateDTO, UUID> {

    public StateController(StateService service) {
        super(service);
    }
}