package com.codeshare.airline.master.airlines.controller;

import com.codeshare.airline.platform.core.dto.master.airline.AllianceDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/alliances")
public class AllianceController extends BaseController<AllianceDTO, UUID> {
    protected AllianceController(BaseService<AllianceDTO, UUID> service) {
        super(service);
    }
}
