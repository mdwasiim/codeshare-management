package com.codeshare.airline.data.codeshare.controller;

import com.codeshare.airline.core.dto.codeshare.CodeshareDeiRuleDTO;
import com.codeshare.airline.data.codeshare.service.CodeshareDeiRuleService;
import com.codeshare.airline.data.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/codeshare-dei-rules")
public class CodeshareDeiRuleController
        extends BaseController<CodeshareDeiRuleDTO, UUID> {

    private final CodeshareDeiRuleService service;

    public CodeshareDeiRuleController(
            CodeshareDeiRuleService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/flight-mapping/{flightMappingId}")
    public List<CodeshareDeiRuleDTO> getByFlightMapping(
            @PathVariable UUID flightMappingId) {

        return service.getByFlightMapping(flightMappingId);
    }
}