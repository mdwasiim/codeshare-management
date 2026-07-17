package com.codeshare.airline.schedule.message.feign;

import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "message-master-data-code-list-client",
        url = "${services.master-data.url:http://localhost:8084}",
        configuration = MasterDataScheduleCodeListClientConfig.class
)
public interface MasterDataScheduleCodeListClient {

    @PostMapping("/internal/schedule-code-lists/validate")
    ScheduleCodeListValidationResponseDTO validate(@RequestBody ScheduleCodeListValidationRequestDTO request);
}
