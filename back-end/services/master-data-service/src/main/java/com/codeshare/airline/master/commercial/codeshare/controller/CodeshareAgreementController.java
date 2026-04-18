package com.codeshare.airline.master.commercial.codeshare.controller;

import com.codeshare.airline.core.dto.codeshare.CodeshareAgreementDTO;
import com.codeshare.airline.master.commercial.codeshare.service.CodeshareAgreementService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/codeshare-agreements")
public class CodeshareAgreementController
        extends BaseController<CodeshareAgreementDTO, UUID> {

    private final CodeshareAgreementService service;

    public CodeshareAgreementController(CodeshareAgreementService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/marketing/{airlineId}")
    public List<CodeshareAgreementDTO> getByMarketingAirline(
            @PathVariable UUID airlineId) {
        return service.getByMarketingAirline(airlineId);
    }

    @GetMapping("/operating/{airlineId}")
    public List<CodeshareAgreementDTO> getByOperatingAirline(
            @PathVariable UUID airlineId) {
        return service.getByOperatingAirline(airlineId);
    }
}