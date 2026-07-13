package com.codeshare.airline.master.flight.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.TrafficRestrictionQualifierDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/traffic-restriction-qualifiers")
public class TrafficRestrictionQualifierController extends BaseController<TrafficRestrictionQualifierDTO, Long> {
    protected TrafficRestrictionQualifierController(BaseService<TrafficRestrictionQualifierDTO, Long> service) {
        super(service);
    }
}
