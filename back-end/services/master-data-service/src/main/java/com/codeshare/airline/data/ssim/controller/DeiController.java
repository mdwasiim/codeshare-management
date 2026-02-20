package com.codeshare.airline.data.ssim.controller;


import com.codeshare.airline.core.dto.ssim.DeiDTO;
import com.codeshare.airline.data.ssim.service.DeiService;
import com.codeshare.airline.data.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/dei")
public class DeiController
        extends BaseController<DeiDTO, UUID> {

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