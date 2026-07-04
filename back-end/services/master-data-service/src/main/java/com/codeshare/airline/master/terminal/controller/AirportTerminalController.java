package com.codeshare.airline.master.terminal.controller;

import com.codeshare.airline.core.dto.master.terminal.AirportTerminalDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/airport-terminals")
public class AirportTerminalController extends BaseController<AirportTerminalDTO, UUID> {

    protected AirportTerminalController(BaseService<AirportTerminalDTO, UUID> service) {
        super(service);
    }
}
