package com.codeshare.airline.master.flightcommercial.passenger.controller;

import com.codeshare.airline.core.dto.master.flightcommercial.passenger.SecureFlightIndicatorDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/secure-flight-indicators")
public class SecureFlightIndicatorController extends BaseController<SecureFlightIndicatorDTO, UUID> {
    protected SecureFlightIndicatorController(BaseService<SecureFlightIndicatorDTO, UUID> service) {
        super(service);
    }
}