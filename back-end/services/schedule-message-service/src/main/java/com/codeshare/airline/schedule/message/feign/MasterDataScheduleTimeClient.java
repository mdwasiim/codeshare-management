package com.codeshare.airline.schedule.message.feign;

import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "message-master-data-schedule-time-client",
        url = "${services.master-data.url:http://localhost:8084}",
        configuration = MasterDataScheduleCodeListClientConfig.class
)
public interface MasterDataScheduleTimeClient {

    @PostMapping("/internal/schedule-time/validate")
    ScheduleTimeValidationResponseDTO validate(@RequestBody ScheduleTimeValidationRequestDTO request);
}
