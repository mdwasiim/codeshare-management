package com.codeshare.airline.inbound.api.controller;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.inbound.api.response.ScheduleFileMessageResponse;
import com.codeshare.airline.inbound.api.response.ScheduleLoadedScheduleDetailResponse;
import com.codeshare.airline.inbound.api.response.ScheduleLoadedScheduleSummaryResponse;
import com.codeshare.airline.inbound.api.response.ScheduleLoadedMessageSummaryResponse;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.domain.enums.SourceType;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFlightDTO;
import com.codeshare.airline.inbound.services.schedule.ScheduleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/schedule/asm-ssm")
@RequiredArgsConstructor
public class AsmSsmScheduleController {

    private final ScheduleQueryService queryService;

    @GetMapping("/loaded-schedules")
    public Page<ScheduleLoadedScheduleSummaryResponse> searchLoadedSchedules(
            @RequestHeader("X-Tenant-Id") String tenantCode,
            @RequestParam(required = false) String type,
            Pageable pageable
    ) {
        return queryService.searchLoadedSchedules(
                messageTypeOrNull(type),
                tenantCode,
                pageable
        );
    }

    @GetMapping("/loaded-schedules/{fileId}")
    public ScheduleLoadedScheduleDetailResponse getLoadedSchedule(
            @RequestHeader("X-Tenant-Id") String tenantCode,
            @PathVariable UUID fileId,
            @RequestParam(required = false) String type
    ) {
        return queryService.getLoadedSchedule(messageTypeOrNull(type), tenantCode, fileId);
    }

    @GetMapping("/{type}/files")
    public Page<ScheduleFileMetaDataDTO> searchFiles(
            @PathVariable String type,
            @RequestParam(required = false) String airlineCode,
            @RequestParam(required = false) ProcessingStatus processingStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant receivedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant receivedTo,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) SourceType sourceType,
            Pageable pageable
    ) {
        return queryService.searchFiles(
                messageType(type),
                airlineCode,
                processingStatus,
                receivedFrom,
                receivedTo,
                fileName,
                sourceType,
                pageable
        );
    }

    @GetMapping("/{type}/messages")
    public Page<ScheduleLoadedMessageSummaryResponse> searchMessages(
            @PathVariable String type,
            @RequestParam(required = false) String airlineCode,
            @RequestParam(required = false) ProcessingStatus processingStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant receivedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant receivedTo,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) SourceType sourceType,
            Pageable pageable
    ) {
        return queryService.searchLoadedMessages(
                messageType(type),
                airlineCode,
                processingStatus,
                receivedFrom,
                receivedTo,
                fileName,
                sourceType,
                pageable
        );
    }

    @GetMapping("/{type}/files/{fileId}")
    public ScheduleFileMetaDataDTO getFile(
            @PathVariable String type,
            @PathVariable UUID fileId
    ) {
        return queryService.getFile(messageType(type), fileId);
    }

    @GetMapping("/{type}/files/{fileId}/schedule")
    public ScheduleFileMessageResponse getParsedSchedule(
            @PathVariable String type,
            @PathVariable UUID fileId
    ) {
        return queryService.getParsedSchedule(messageType(type), fileId);
    }

    @GetMapping("/{type}/files/{fileId}/flights")
    public Page<ScheduleFlightDTO> searchFlights(
            @PathVariable String type,
            @PathVariable UUID fileId,
            @RequestParam(required = false) String airlineCode,
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String aircraftType,
            @RequestParam(required = false) String serviceType,
            Pageable pageable
    ) {
        return queryService.searchFlights(
                messageType(type),
                fileId,
                airlineCode,
                flightNumber,
                origin,
                destination,
                aircraftType,
                serviceType,
                pageable
        );
    }

    @GetMapping("/{type}/flights/{flightId}")
    public ScheduleFlightDTO getFlight(
            @PathVariable String type,
            @PathVariable UUID flightId
    ) {
        return queryService.getFlight(messageType(type), flightId);
    }

    private MessageType messageTypeOrNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return messageType(value);
    }

    private MessageType messageType(String value) {
        return MessageType.valueOf(value.trim().toUpperCase());
    }
}
