package com.codeshare.airline.data.core.controller;

import com.codeshare.airline.core.dto.georegion.CountryDTO;
import com.codeshare.airline.data.common.BaseController;
import com.codeshare.airline.data.core.service.CountryService;
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