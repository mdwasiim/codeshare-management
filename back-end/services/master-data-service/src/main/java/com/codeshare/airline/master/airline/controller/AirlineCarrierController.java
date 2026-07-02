package com.codeshare.airline.master.airline.controller;

import com.codeshare.airline.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/airline-carriers")
public class AirlineCarrierController extends BaseController<AirlineCarrierDTO, UUID> {
    protected AirlineCarrierController(BaseService<AirlineCarrierDTO, UUID> service) {
        super(service);
    }
}
