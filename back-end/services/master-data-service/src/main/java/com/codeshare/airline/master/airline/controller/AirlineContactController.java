package com.codeshare.airline.master.airline.controller;

import com.codeshare.airline.core.dto.master.airline.AirlineContactDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/airline-contacts")
public class AirlineContactController extends BaseController<AirlineContactDTO, UUID> {
    protected AirlineContactController(BaseService<AirlineContactDTO, UUID> service) {
        super(service);
    }
}
