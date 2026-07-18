package com.codeshare.airline.schedule.ingestion.integration.masterdata;

import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "master-data-reference-client",
        url = "${master-data.service.url:http://localhost:8084}"
)
public interface MasterDataReferenceClient {

    @PostMapping("/internal/schedule-code-lists/validate")
    ScheduleCodeListValidationResponseDTO validateScheduleCodeLists(
            @RequestBody ScheduleCodeListValidationRequestDTO request
    );
}
