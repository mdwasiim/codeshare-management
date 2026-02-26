package com.codeshare.airline.schedule.api.controller;

import com.codeshare.airline.schedule.api.UploadResponse;
import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.orchestration.ScheduleIngestionProcessor;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import com.codeshare.airline.schedule.domain.common.ScheduleSourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleUploadController {

    private final ScheduleIngestionProcessor processor;

    @PostMapping("/upload")
    public UploadResponse upload(
            @RequestParam MultipartFile file,
            @RequestParam(required = false) ScheduleMessageType expectedType
    ) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Empty schedule file");
        }

        ScheduleSourceFile sourceFile = ScheduleSourceFile.builder()
                .fileId(UUID.randomUUID().toString())
                .fileName(file.getOriginalFilename())
                .sourceType(ScheduleSourceType.REST)
                .sourceSystem("HTTP_UPLOAD")
                .receivedAt(Instant.now())
                .scheduleMessageType(expectedType) // optional
                .streamSupplier(() -> {
                    try {
                        return file.getInputStream();
                    } catch (IOException e) {
                        throw new IllegalStateException("Failed to open input stream", e);
                    }
                })
                .build();

        processor.process(sourceFile);

        return UploadResponse.accepted(sourceFile.getFileId());
    }
}