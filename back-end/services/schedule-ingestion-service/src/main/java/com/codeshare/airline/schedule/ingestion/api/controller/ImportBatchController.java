package com.codeshare.airline.schedule.ingestion.api.controller;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportBatchDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportedScheduleDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.application.projection.ImportBatchQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/schedule/internal")
@RequiredArgsConstructor
public class ImportBatchController {

    private final ImportBatchQueryService importBatchQueryService;

    @GetMapping("/import-batches/{importBatchId}")
    public ImportBatchDTO getImportBatch(@PathVariable UUID importBatchId) {
        return importBatchQueryService.getImportBatch(importBatchId);
    }

    @GetMapping("/imported-schedules/{type}/{fileId}")
    public ImportedScheduleDTO getImportedSchedule(
            @PathVariable MessageType type,
            @PathVariable UUID fileId
    ) {
        return importBatchQueryService.getImportedSchedule(type, fileId);
    }
}

