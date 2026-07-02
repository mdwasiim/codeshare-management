package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.core.dto.master.schedule.OperationalSuffixDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/operational-suffixes")
public class OperationalSuffixController extends BaseController<OperationalSuffixDTO, UUID> {

    protected OperationalSuffixController(BaseService<OperationalSuffixDTO, UUID> service) {
        super(service);
    }
}
