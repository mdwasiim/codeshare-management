package com.codeshare.airline.master.flight.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.FlightFrequencyDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/flight-frequencies")
public class FlightFrequencyController extends BaseController<FlightFrequencyDTO, Long> {
    protected FlightFrequencyController(BaseService<FlightFrequencyDTO, Long> service) {
        super(service);
    }
}