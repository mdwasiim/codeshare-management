package com.codeshare.airline.data.codeshare.controller;

import com.codeshare.airline.core.dto.codeshare.CodeshareDayRuleDTO;
import com.codeshare.airline.data.codeshare.service.CodeshareDayRuleService;
import com.codeshare.airline.data.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/codeshare-day-rules")
public class CodeshareDayRuleController
        extends BaseController<CodeshareDayRuleDTO, UUID> {

    private final CodeshareDayRuleService service;

    public CodeshareDayRuleController(CodeshareDayRuleService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/mapping/{mappingId}")
    public List<CodeshareDayRuleDTO> getByMapping(
            @PathVariable UUID mappingId) {
        return service.getByFlightMapping(mappingId);
    }
}