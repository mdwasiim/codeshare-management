package com.codeshare.airline.messaging.controller;

import com.codeshare.airline.dto.airport.georegion.ServiceTypeDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/service-types")
public class ServiceTypeController extends BaseController<ServiceTypeDTO, UUID> {

    protected ServiceTypeController(BaseService<ServiceTypeDTO, UUID> service) {
        super(service);
    }
}