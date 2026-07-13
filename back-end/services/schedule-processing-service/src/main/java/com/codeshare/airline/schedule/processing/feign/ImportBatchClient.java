package com.codeshare.airline.schedule.processing.feign;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportBatchDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportedScheduleDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "import-batch-client",
        url = "${services.ingestion.url:http://localhost:8082}"
)
public interface ImportBatchClient {

    @GetMapping("/schedule/internal/import-batches/{importBatchId}")
    ImportBatchDTO getImportBatch(@PathVariable("importBatchId") UUID importBatchId);

    @GetMapping("/schedule/internal/imported-schedules/{type}/{fileId}")
    ImportedScheduleDTO getImportedSchedule(
            @PathVariable("type") MessageType type,
            @PathVariable("fileId") UUID fileId
    );
}

