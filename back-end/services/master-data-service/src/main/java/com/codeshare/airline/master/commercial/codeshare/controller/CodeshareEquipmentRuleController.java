package com.codeshare.airline.master.commercial.codeshare.controller;

import com.codeshare.airline.core.dto.codeshare.CodeshareEquipmentRuleDTO;
import com.codeshare.airline.master.commercial.codeshare.service.CodeshareEquipmentRuleService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/codeshare-equipment-rules")
public class CodeshareEquipmentRuleController
        extends BaseController<CodeshareEquipmentRuleDTO, UUID> {

    private final CodeshareEquipmentRuleService service;

    public CodeshareEquipmentRuleController(
            CodeshareEquipmentRuleService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/flight-mapping/{flightMappingId}")
    public List<CodeshareEquipmentRuleDTO> getByFlightMapping(
            @PathVariable UUID flightMappingId) {

        return service.getByFlightMapping(flightMappingId);
    }
}