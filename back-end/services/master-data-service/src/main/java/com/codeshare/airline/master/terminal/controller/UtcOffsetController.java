package com.codeshare.airline.master.terminal.controller;

import com.codeshare.airline.platform.core.dto.master.terminal.UtcOffsetDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/utc-offsets")
public class UtcOffsetController extends BaseController<UtcOffsetDTO, Long> {

    protected UtcOffsetController(BaseService<UtcOffsetDTO, Long> service) {
        super(service);
    }
}
