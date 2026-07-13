package com.codeshare.airline.master.flight.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.TrafficRestrictionCodeDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/traffic-restriction-codes")
public class TrafficRestrictionCodeController extends BaseController<TrafficRestrictionCodeDTO, Long> {
    protected TrafficRestrictionCodeController(BaseService<TrafficRestrictionCodeDTO, Long> service) {
        super(service);
    }
}