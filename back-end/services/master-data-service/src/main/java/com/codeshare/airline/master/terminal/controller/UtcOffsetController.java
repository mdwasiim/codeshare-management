package com.codeshare.airline.master.terminal.controller;

import com.codeshare.airline.core.dto.master.terminal.UtcOffsetDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/utc-offsets")
public class UtcOffsetController extends BaseController<UtcOffsetDTO, UUID> {

    protected UtcOffsetController(BaseService<UtcOffsetDTO, UUID> service) {
        super(service);
    }
}
