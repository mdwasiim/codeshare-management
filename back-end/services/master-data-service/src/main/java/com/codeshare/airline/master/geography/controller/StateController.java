package com.codeshare.airline.master.geography.controller;

import com.codeshare.airline.platform.core.dto.master.georegion.StateDTO;
import com.codeshare.airline.master.geography.service.StateService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/states")
public class StateController
        extends BaseController<StateDTO, Long> {

    public StateController(StateService service) {
        super(service);
    }
}