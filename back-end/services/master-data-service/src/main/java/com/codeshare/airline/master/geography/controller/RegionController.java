package com.codeshare.airline.master.geography.controller;

import com.codeshare.airline.core.dto.master.georegion.RegionDTO;
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