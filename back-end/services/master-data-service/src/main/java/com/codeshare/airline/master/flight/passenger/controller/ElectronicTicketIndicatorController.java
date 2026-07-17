package com.codeshare.airline.master.flight.passenger.controller;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger.ElectronicTicketIndicatorDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/electronic-ticket-indicators")
public class ElectronicTicketIndicatorController extends BaseController<ElectronicTicketIndicatorDTO, Long> {
    protected ElectronicTicketIndicatorController(BaseService<ElectronicTicketIndicatorDTO, Long> service) {
        super(service);
    }
}