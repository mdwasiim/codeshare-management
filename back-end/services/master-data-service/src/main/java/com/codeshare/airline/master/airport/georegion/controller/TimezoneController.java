package com.codeshare.airline.master.airport.georegion.controller;

import com.codeshare.airline.core.dto.airport.georegion.TimezoneDTO;
import com.codeshare.airline.master.airport.georegion.service.TimezoneService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/timezones")
public class TimezoneController
        extends BaseController<TimezoneDTO, UUID> {

    public TimezoneController(TimezoneService service) {
        super(service);
    }
}