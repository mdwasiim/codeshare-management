package com.codeshare.airline.master.flight.passenger.controller;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger.ServiceTypeDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/flight-commercial-service-types")
public class ServiceTypeController extends BaseController<ServiceTypeDTO, Long> {
    protected ServiceTypeController(BaseService<ServiceTypeDTO, Long> service) {
        super(service);
    }
}