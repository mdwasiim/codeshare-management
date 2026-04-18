package com.codeshare.airline.master.airport.georegion.controller;

import com.codeshare.airline.dto.airport.georegion.DstRuleDTO;
import com.codeshare.airline.master.airport.georegion.service.DstRuleService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/dst-rules")
public class DstRuleController
        extends BaseController<DstRuleDTO, UUID> {

    public DstRuleController(DstRuleService service) {
        super(service);
    }
}