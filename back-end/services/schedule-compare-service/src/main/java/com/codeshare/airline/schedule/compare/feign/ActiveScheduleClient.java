package com.codeshare.airline.schedule.compare.feign;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ActiveScheduleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "active-schedule-client",
        url = "${services.schedule.url:http://localhost:8087}"
)
public interface ActiveScheduleClient {

    @GetMapping("/schedule/internal/active-schedules/{airlineCode}")
    ActiveScheduleDTO getActiveSchedule(@PathVariable("airlineCode") String airlineCode);
}

