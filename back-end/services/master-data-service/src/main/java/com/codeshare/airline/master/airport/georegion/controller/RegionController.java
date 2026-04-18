package com.codeshare.airline.master.airport.georegion.controller;

import com.codeshare.airline.core.dto.airport.georegion.RegionDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/regions")
public class RegionController extends BaseController<RegionDTO, UUID> {

    protected RegionController(BaseService<RegionDTO, UUID> service) {
        super(service);
    }

}