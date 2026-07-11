package com.codeshare.airline.schedule.ingestion.api.controller;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.api.request.ScheduleMessageTextRequest;
import com.codeshare.airline.schedule.ingestion.api.response.ScheduleMessageIngestionResponse;
import com.codeshare.airline.schedule.ingestion.api.response.ScheduleMessageValidationResponse;
import com.codeshare.airline.schedule.ingestion.api.service.ScheduleMessageApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/schedule/messages")
@RequiredArgsConstructor
public class ScheduleMessageController {

    private final ScheduleMessageApiService service;

    @PostMapping(value = "/{type}/validate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ScheduleMessageValidationResponse validateFile(
            @PathVariable String type,
            @RequestParam MultipartFile file,
            @RequestParam String airlineCode
    ) throws IOException {
        return service.validate(messageType(type), airlineCode, file.getOriginalFilename(), file.getBytes());
    }

    @PostMapping(value = "/{type}/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ScheduleMessageValidationResponse validateText(
            @PathVariable String type,
            @Valid @RequestBody ScheduleMessageTextRequest request
    ) {
        return service.validate(
                messageType(type),
                request.getAirlineCode(),
                request.getFileName(),
                service.textToBytes(request.getContent())
        );
    }

    @PostMapping(value = "/{type}/parse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ScheduleMessageValidationResponse parseFile(
            @PathVariable String type,
            @RequestParam MultipartFile file,
            @RequestParam String airlineCode
    ) throws IOException {
        return service.parse(messageType(type), airlineCode, file.getOriginalFilename(), file.getBytes());
    }

    @PostMapping(value = "/{type}/parse", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ScheduleMessageValidationResponse parseText(
            @PathVariable String type,
            @Valid @RequestBody ScheduleMessageTextRequest request
    ) {
        return service.parse(
                messageType(type),
                request.getAirlineCode(),
                request.getFileName(),
                service.textToBytes(request.getContent())
        );
    }

    @PostMapping(value = "/{type}/ingest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ScheduleMessageIngestionResponse ingestFile(
            @PathVariable String type,
            @RequestParam MultipartFile file,
            @RequestParam String airlineCode
    ) throws IOException {
        return service.ingest(messageType(type), airlineCode, file.getOriginalFilename(), file.getBytes());
    }

    @PostMapping(value = "/{type}/ingest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ScheduleMessageIngestionResponse ingestText(
            @PathVariable String type,
            @Valid @RequestBody ScheduleMessageTextRequest request
    ) {
        return service.ingest(
                messageType(type),
                request.getAirlineCode(),
                request.getFileName(),
                service.textToBytes(request.getContent())
        );
    }

    private MessageType messageType(String value) {
        return MessageType.valueOf(value.trim().toUpperCase());
    }
}
