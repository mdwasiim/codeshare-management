package com.codeshare.airline.master.flightcommercial.schedule.controller;

import com.codeshare.airline.core.dto.master.flightcommercial.schedule.FlightSuffixDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/flight-suffixes")
public class FlightSuffixController extends BaseController<FlightSuffixDTO, UUID> {
    protected FlightSuffixController(BaseService<FlightSuffixDTO, UUID> service) {
        super(service);
    }
}