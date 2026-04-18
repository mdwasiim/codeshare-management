package com.codeshare.airline.master.airport.georegion.controller;

import com.codeshare.airline.dto.airport.georegion.SeasonDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/seasons")
public class SeasonController extends BaseController<SeasonDTO, UUID> {

    protected SeasonController(BaseService<SeasonDTO, UUID> service) {
        super(service);
    }
}