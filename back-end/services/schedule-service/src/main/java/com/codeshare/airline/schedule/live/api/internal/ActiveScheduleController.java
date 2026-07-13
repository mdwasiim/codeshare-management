package com.codeshare.airline.schedule.live.api.internal;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ActiveScheduleDTO;
import com.codeshare.airline.schedule.live.application.ActiveScheduleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule/internal")
@RequiredArgsConstructor
public class ActiveScheduleController {

    private final ActiveScheduleQueryService activeScheduleQueryService;

    @GetMapping("/active-schedules/{airlineCode}")
    public ActiveScheduleDTO getActiveSchedule(@PathVariable String airlineCode) {
        return activeScheduleQueryService.getActiveSchedule(airlineCode);
    }

    @GetMapping("/operational-schedules/{airlineCode}")
    public ActiveScheduleDTO getOperationalSchedule(@PathVariable String airlineCode) {
        return activeScheduleQueryService.getActiveSchedule(airlineCode);
    }
}

