package com.codeshare.airline.data.core.controller;

import com.codeshare.airline.core.dto.georegion.RegionDTO;
import com.codeshare.airline.data.common.BaseController;
import com.codeshare.airline.persistence.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/regions")
public class RegionController extends BaseController<RegionDTO, UUID> {

    protected RegionController(BaseService<RegionDTO, UUID> service) {
        super(service);
    }

}