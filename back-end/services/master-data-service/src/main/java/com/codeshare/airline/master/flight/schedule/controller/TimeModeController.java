package com.codeshare.airline.master.flight.schedule.controller;

import com.codeshare.airline.core.dto.master.flightcommercial.schedule.TimeModeDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/time-modes")
public class TimeModeController extends BaseController<TimeModeDTO, UUID> {
    protected TimeModeController(BaseService<TimeModeDTO, UUID> service) {
        super(service);
    }
}