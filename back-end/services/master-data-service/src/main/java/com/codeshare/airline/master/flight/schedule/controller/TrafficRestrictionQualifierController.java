package com.codeshare.airline.master.flight.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.TrafficRestrictionQualifierDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/traffic-restriction-qualifiers")
public class TrafficRestrictionQualifierController extends BaseController<TrafficRestrictionQualifierDTO, UUID> {
    protected TrafficRestrictionQualifierController(BaseService<TrafficRestrictionQualifierDTO, UUID> service) {
        super(service);
    }
}
