package com.codeshare.airline.master.airlines.controller;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineContactDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/airline-contacts")
public class AirlineContactController extends BaseController<AirlineContactDTO, Long> {
    protected AirlineContactController(BaseService<AirlineContactDTO, Long> service) {
        super(service);
    }
}
