package com.codeshare.airline.data.airport.georegion.controller;

import com.codeshare.airline.core.dto.airport.georegion.CountryDTO;
import com.codeshare.airline.data.airport.georegion.service.CountryService;
import com.codeshare.airline.data.common.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/countries")
public class CountryController
        extends BaseController<CountryDTO, UUID> {

    public CountryController(CountryService service) {
        super(service);
    }
}