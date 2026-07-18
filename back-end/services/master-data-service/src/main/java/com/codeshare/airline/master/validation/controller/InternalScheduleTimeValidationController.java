package com.codeshare.airline.master.validation.controller;

import com.codeshare.airline.master.validation.service.ScheduleTimeValidationService;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/schedule-time")
@RequiredArgsConstructor
public class InternalScheduleTimeValidationController {

    private final ScheduleTimeValidationService service;

    @PostMapping("/validate")
    public ScheduleTimeValidationResponseDTO validate(@RequestBody ScheduleTimeValidationRequestDTO request) {
        return service.validate(request);
    }
}
