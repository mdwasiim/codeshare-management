package com.codeshare.airline.data.core.controller;

import com.codeshare.airline.core.dto.georegion.ServiceTypeDTO;
import com.codeshare.airline.data.common.BaseController;
import com.codeshare.airline.persistence.persistence.service.BaseService;
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