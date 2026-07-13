package com.codeshare.airline.master.terminal.controller;

import com.codeshare.airline.platform.core.dto.master.terminal.DaylightSavingRuleDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/daylight-saving-rules")
public class DaylightSavingRuleController extends BaseController<DaylightSavingRuleDTO, Long> {

    protected DaylightSavingRuleController(BaseService<DaylightSavingRuleDTO, Long> service) {
        super(service);
    }
}
