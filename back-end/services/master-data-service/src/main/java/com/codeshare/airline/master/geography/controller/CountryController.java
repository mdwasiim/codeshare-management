package com.codeshare.airline.master.geography.controller;

import com.codeshare.airline.core.dto.master.georegion.CountryDTO;
import com.codeshare.airline.master.geography.service.CountryService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/countries")
public class CountryController
        extends BaseController<CountryDTO, UUID> {

    public CountryController(CountryService service) {
        super(service);
    }
}