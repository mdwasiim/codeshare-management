package com.codeshare.airline.master.geography.controller;

import com.codeshare.airline.platform.core.dto.master.georegion.RegionDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/regions")
public class RegionController extends BaseController<RegionDTO, Long> {

    protected RegionController(BaseService<RegionDTO, Long> service) {
        super(service);
    }

}