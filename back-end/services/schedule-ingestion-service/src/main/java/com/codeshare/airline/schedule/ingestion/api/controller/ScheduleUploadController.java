package com.codeshare.airline.schedule.ingestion.api.controller;

import com.codeshare.airline.schedule.ingestion.api.response.UploadResponse;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import lombok.RequiredArgsConstructor;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleUploadController {

    private final ProducerTemplate producerTemplate;

    @PostMapping("/upload")
    public UploadResponse upload(
            @RequestParam MultipartFile file,
            @RequestParam String airlineCode,
            @RequestParam(required = false) MessageType expectedType
    ) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Empty schedule file");
        }

        byte[] content = file.getBytes();

        producerTemplate.sendBodyAndHeaders(
                "seda:schedule-processing",
                content,
                java.util.Map.of(
                        "AIRLINE_CODE", airlineCode,
                        "MESSAGE_TYPE", expectedType != null ? expectedType.name() : "UNKNOWN",
                        "SOURCE_TYPE", "REST",
                        "FILE_NAME", file.getOriginalFilename(),
                        "RECEIVED_AT", Instant.now().toString()
                )
        );

        return UploadResponse.accepted(file.getOriginalFilename());
    }
}