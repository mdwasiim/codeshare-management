package com.codeshare.airline.master.airlines.controller;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/airline-carriers")
public class AirlineCarrierController extends BaseController<AirlineCarrierDTO, Long> {
    protected AirlineCarrierController(BaseService<AirlineCarrierDTO, Long> service) {
        super(service);
    }
}
