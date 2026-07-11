package com.codeshare.airline.master.terminal.controller;

import com.codeshare.airline.platform.core.dto.master.terminal.DaylightSavingRuleDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/daylight-saving-rules")
public class DaylightSavingRuleController extends BaseController<DaylightSavingRuleDTO, UUID> {

    protected DaylightSavingRuleController(BaseService<DaylightSavingRuleDTO, UUID> service) {
        super(service);
    }
}
