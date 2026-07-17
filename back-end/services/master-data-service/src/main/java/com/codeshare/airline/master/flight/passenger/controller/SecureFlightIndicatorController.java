package com.codeshare.airline.master.flight.passenger.controller;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger.SecureFlightIndicatorDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/secure-flight-indicators")
public class SecureFlightIndicatorController extends BaseController<SecureFlightIndicatorDTO, Long> {
    protected SecureFlightIndicatorController(BaseService<SecureFlightIndicatorDTO, Long> service) {
        super(service);
    }
}