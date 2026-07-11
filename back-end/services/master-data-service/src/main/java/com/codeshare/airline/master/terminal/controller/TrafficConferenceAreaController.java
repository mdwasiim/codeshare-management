package com.codeshare.airline.master.terminal.controller;

import com.codeshare.airline.platform.core.dto.master.terminal.TrafficConferenceAreaDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/traffic-conference-areas")
public class TrafficConferenceAreaController extends BaseController<TrafficConferenceAreaDTO, UUID> {

    protected TrafficConferenceAreaController(BaseService<TrafficConferenceAreaDTO, UUID> service) {
        super(service);
    }
}
