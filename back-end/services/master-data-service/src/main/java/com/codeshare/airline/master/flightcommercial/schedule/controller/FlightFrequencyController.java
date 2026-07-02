package com.codeshare.airline.master.flightcommercial.schedule.controller;

import com.codeshare.airline.core.dto.master.flightcommercial.schedule.FlightFrequencyDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/flight-frequencies")
public class FlightFrequencyController extends BaseController<FlightFrequencyDTO, UUID> {
    protected FlightFrequencyController(BaseService<FlightFrequencyDTO, UUID> service) {
        super(service);
    }
}