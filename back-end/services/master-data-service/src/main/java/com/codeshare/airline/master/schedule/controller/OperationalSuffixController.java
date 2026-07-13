package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.schedule.OperationalSuffixDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/operational-suffixes")
public class OperationalSuffixController extends BaseController<OperationalSuffixDTO, Long> {

    protected OperationalSuffixController(BaseService<OperationalSuffixDTO, Long> service) {
        super(service);
    }
}
