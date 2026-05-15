package com.codeshare.airline.inbound.api.controller;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.api.response.ScheduleFileMessageResponse;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/schedule/messages/{type}")
@RequiredArgsConstructor
public class ScheduleQueryController {

    private final ScheduleQueryService queryService;

    @GetMapping("/files")
    public Page<ScheduleFileMetaDataDTO> searchFiles(
            @PathVariable String type,
            @RequestParam(required = false) String airlineCode,
            @RequestParam(required = false) ProcessingStatus processingStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant receivedFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant receivedTo,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) String checksum,
            Pageable pageable
    ) {
        return queryService.searchFiles(
                messageType(type),
                airlineCode,
                processingStatus,
                receivedFrom,
                receivedTo,
                fileName,
                checksum,
                pageable
        );
    }

    @GetMapping("/files/{fileId}")
    public ScheduleFileMetaDataDTO getFile(
            @PathVariable String type,
            @PathVariable UUID fileId
    ) {
        return queryService.getFile(messageType(type), fileId);
    }

    @GetMapping("/files/{fileId}/schedule")
    public ScheduleFileMessageResponse getParsedSchedule(
            @PathVariable String type,
            @PathVariable UUID fileId
    ) {
        return queryService.getParsedSchedule(messageType(type), fileId);
    }

    @GetMapping("/files/{fileId}/flights")
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

    @GetMapping("/flights/{flightId}")
    public ScheduleFlightDTO getFlight(
            @PathVariable String type,
            @PathVariable UUID flightId
    ) {
        return queryService.getFlight(messageType(type), flightId);
    }

    private MessageType messageType(String value) {
        return MessageType.valueOf(value.trim().toUpperCase());
    }
}
