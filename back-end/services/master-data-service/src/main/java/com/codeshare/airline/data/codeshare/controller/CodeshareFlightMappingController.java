package com.codeshare.airline.data.codeshare.controller;

import com.codeshare.airline.core.dto.codeshare.CodeshareFlightMappingDTO;
import com.codeshare.airline.data.codeshare.service.CodeshareFlightMappingService;
import com.codeshare.airline.data.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/codeshare-flight-mappings")
public class CodeshareFlightMappingController
        extends BaseController<CodeshareFlightMappingDTO, UUID> {

    private final CodeshareFlightMappingService service;

    public CodeshareFlightMappingController(
            CodeshareFlightMappingService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/agreement/{agreementId}")
    public List<CodeshareFlightMappingDTO> getByAgreement(
            @PathVariable UUID agreementId) {
        return service.getByAgreement(agreementId);
    }
}