package com.codeshare.airline.data.core.controller;

import com.codeshare.airline.core.dto.georegion.TimezoneDTO;
import com.codeshare.airline.data.common.BaseController;
import com.codeshare.airline.data.core.service.TimezoneService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/timezones")
public class TimezoneController
        extends BaseController<TimezoneDTO, UUID> {

    public TimezoneController(TimezoneService service) {
        super(service);
    }
}