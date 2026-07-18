package com.codeshare.airline.master.validation.controller;

import com.codeshare.airline.master.validation.service.ScheduleCodeListValidationService;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/schedule-code-lists")
@RequiredArgsConstructor
public class InternalScheduleCodeListValidationController {

    private final ScheduleCodeListValidationService service;

    @PostMapping("/validate")
    public ScheduleCodeListValidationResponseDTO validate(@RequestBody ScheduleCodeListValidationRequestDTO request) {
        return service.validate(request);
    }
}
