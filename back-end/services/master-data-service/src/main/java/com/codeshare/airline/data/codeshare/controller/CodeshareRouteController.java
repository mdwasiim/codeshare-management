package com.codeshare.airline.data.codeshare.controller;

import com.codeshare.airline.core.dto.codeshare.CodeshareRouteDTO;
import com.codeshare.airline.data.codeshare.service.CodeshareRouteService;
import com.codeshare.airline.data.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/codeshare-routes")
public class CodeshareRouteController
        extends BaseController<CodeshareRouteDTO, UUID> {

    private final CodeshareRouteService service;

    public CodeshareRouteController(CodeshareRouteService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/agreement/{agreementId}")
    public List<CodeshareRouteDTO> getByAgreement(
            @PathVariable UUID agreementId) {

        return service.getByAgreement(agreementId);
    }
}