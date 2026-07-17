package com.codeshare.airline.master.airlines.controller;

import com.codeshare.airline.platform.core.dto.master.airline.AllianceDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/alliances")
public class AllianceController extends BaseController<AllianceDTO, Long> {
    protected AllianceController(BaseService<AllianceDTO, Long> service) {
        super(service);
    }
}
