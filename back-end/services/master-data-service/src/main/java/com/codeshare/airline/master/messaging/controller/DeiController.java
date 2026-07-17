package com.codeshare.airline.master.messaging.controller;


import com.codeshare.airline.platform.core.dto.master.messaging.DeiDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.messaging.service.DeiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/dei")
public class DeiController
        extends BaseController<DeiDTO, Long> {

    private final DeiService service;

    public DeiController(DeiService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/number/{deiNumber}")
    public DeiDTO getByDeiNumber(
            @PathVariable String deiNumber) {

        return service.getByDeiNumber(deiNumber)
                .orElseThrow(() ->
                        new RuntimeException("DEI not found"));
    }
}