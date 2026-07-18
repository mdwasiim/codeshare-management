package com.codeshare.airline.schedule.ingestion.api.controller;

import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.platform.core.enums.schedule.SourceType;
import com.codeshare.airline.schedule.ingestion.api.request.SsimFlightSearchCriteria;
import com.codeshare.airline.schedule.ingestion.api.response.SsimLoadedScheduleDetailResponse;
import com.codeshare.airline.schedule.ingestion.api.response.SsimLoadedScheduleSummaryResponse;
import com.codeshare.airline.schedule.ingestion.api.response.SsimValidationReportRowResponse;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimFlightDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.persistence.services.ssim.SsimScheduleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/schedule/ssim")
@RequiredArgsConstructor
public class SsimScheduleController {

    private final SsimScheduleQueryService queryService;

    @GetMapping("/files")
    public Page<SsimMetaDataDTO> searchFiles(
            @RequestParam(required = false) String airlineCode,
            @RequestParam(required = false) ProcessingStatus processingStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant receivedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant receivedTo,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) SourceType sourceType,
            Pageable pageable
    ) {
        return queryService.searchFiles(
                airlineCode,
                processingStatus,
                receivedFrom,
                receivedTo,
                fileName,
                sourceType,
                pageable
        );
    }

    @GetMapping("/loaded-schedules")
    public Page<SsimLoadedScheduleSummaryResponse> searchLoadedSchedules(
            @RequestHeader("X-Tenant-Id") String tenantCode,
            Pageable pageable
    ) {
        return queryService.searchLoadedSchedules(tenantCode, pageable);
    }

    @GetMapping("/loaded-schedules/{fileId}")
    public SsimLoadedScheduleDetailResponse getLoadedSchedule(
            @RequestHeader("X-Tenant-Id") String tenantCode,
            @PathVariable UUID fileId
    ) {
        return queryService.getLoadedSchedule(tenantCode, fileId);
    }

    @GetMapping("/files/{fileId}")
    public SsimMetaDataDTO getFile(@PathVariable UUID fileId) {
        return queryService.getFile(fileId);
    }

    @GetMapping("/files/{fileId}/message")
    public SSIMMessageDTO getMessage(@PathVariable UUID fileId) {
        return queryService.getMessage(fileId);
    }

    @GetMapping("/files/{fileId}/validation-report")
    public List<SsimValidationReportRowResponse> getValidationReport(@PathVariable UUID fileId) {
        return queryService.getValidationReport(fileId);
    }

    @GetMapping("/files/{fileId}/flights")
    public Page<SsimFlightDTO> searchFlights(
            @PathVariable UUID fileId,
            SsimFlightSearchCriteria criteria,
            Pageable pageable
    ) {
        criteria.setFileId(fileId);
        return queryService.searchFlights(criteria, pageable);
    }

    @GetMapping("/flights/{flightId}")
    public SsimFlightDTO getFlight(@PathVariable Long flightId) {
        return queryService.getFlight(flightId);
    }
}

