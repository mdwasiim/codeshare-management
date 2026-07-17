package com.codeshare.airline.master.flight.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.FlightSuffixDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/flight-suffixes")
public class FlightSuffixController extends BaseController<FlightSuffixDTO, Long> {
    protected FlightSuffixController(BaseService<FlightSuffixDTO, Long> service) {
        super(service);
    }
}