package com.codeshare.airline.data.core.controller;

import com.codeshare.airline.core.dto.georegion.DstRuleDTO;
import com.codeshare.airline.data.common.BaseController;
import com.codeshare.airline.data.core.service.DstRuleService;
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