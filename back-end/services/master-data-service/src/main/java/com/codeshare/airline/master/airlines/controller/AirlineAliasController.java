package com.codeshare.airline.master.airlines.controller;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineAliasDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/airline-aliases")
public class AirlineAliasController extends BaseController<AirlineAliasDTO, Long> {
    protected AirlineAliasController(BaseService<AirlineAliasDTO, Long> service) {
        super(service);
    }
}
