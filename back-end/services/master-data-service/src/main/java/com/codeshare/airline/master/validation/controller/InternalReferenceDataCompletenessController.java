package com.codeshare.airline.master.validation.controller;

import com.codeshare.airline.master.validation.service.ReferenceDataCompletenessService;
import com.codeshare.airline.platform.core.dto.master.validation.ReferenceDataCompletenessResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/reference-data-completeness")
@RequiredArgsConstructor
public class InternalReferenceDataCompletenessController {

    private final ReferenceDataCompletenessService service;

    @GetMapping("/outbound-schedule")
    public ReferenceDataCompletenessResponseDTO outboundSchedule() {
        return service.outboundSchedule();
    }
}
