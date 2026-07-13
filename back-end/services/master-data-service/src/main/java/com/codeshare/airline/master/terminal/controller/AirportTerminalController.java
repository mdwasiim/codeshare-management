package com.codeshare.airline.master.terminal.controller;

import com.codeshare.airline.platform.core.dto.master.terminal.AirportTerminalDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/airport-terminals")
public class AirportTerminalController extends BaseController<AirportTerminalDTO, Long> {

    protected AirportTerminalController(BaseService<AirportTerminalDTO, Long> service) {
        super(service);
    }
}
