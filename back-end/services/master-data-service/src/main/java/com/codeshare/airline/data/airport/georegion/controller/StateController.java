package com.codeshare.airline.data.airport.georegion.controller;

import com.codeshare.airline.core.dto.airport.georegion.StateDTO;
import com.codeshare.airline.data.airport.georegion.service.StateService;
import com.codeshare.airline.data.common.BaseController;
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