package com.codeshare.airline.master.georegion.controller;

import com.codeshare.airline.core.dto.master.georegion.DstRuleDTO;
import com.codeshare.airline.master.georegion.service.DstRuleService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/dst-rules")
public class DstRuleController
        extends BaseController<DstRuleDTO, UUID> {

    public DstRuleController(DstRuleService service) {
        super(service);
    }
}