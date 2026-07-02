package com.codeshare.airline.master.airline.controller;

import com.codeshare.airline.core.dto.master.airline.AirlineBusinessRoleDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/airline-business-roles")
public class AirlineBusinessRoleController extends BaseController<AirlineBusinessRoleDTO, UUID> {
    protected AirlineBusinessRoleController(BaseService<AirlineBusinessRoleDTO, UUID> service) {
        super(service);
    }
}
