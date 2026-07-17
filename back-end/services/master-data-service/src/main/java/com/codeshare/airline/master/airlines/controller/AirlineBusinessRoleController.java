package com.codeshare.airline.master.airlines.controller;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineBusinessRoleDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/airline-business-roles")
public class AirlineBusinessRoleController extends BaseController<AirlineBusinessRoleDTO, Long> {
    protected AirlineBusinessRoleController(BaseService<AirlineBusinessRoleDTO, Long> service) {
        super(service);
    }
}
