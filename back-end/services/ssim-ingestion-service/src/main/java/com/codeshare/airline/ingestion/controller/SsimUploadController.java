package com.codeshare.airline.ingestion.controller;

import com.codeshare.airline.ingestion.model.SsimRawFile;
import com.codeshare.airline.ingestion.model.SsimSourceMetadata;
import com.codeshare.airline.ingestion.parsing.SsimParsingPipeline;
import com.codeshare.airline.ingestion.source.SourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/ssim")
@RequiredArgsConstructor
public class SsimUploadController {

    private final SsimParsingPipeline pipeline;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file) throws IOException {

        SsimRawFile raw = SsimRawFile.builder()
                .fileId(UUID.randomUUID().toString())
                .originalFileName(file.getOriginalFilename())
                .content(file.getBytes())
                .sourceType(SourceType.REST)
                .receivedAt(Instant.now())
                .metadata(SsimSourceMetadata.builder().build())
                .build();


        pipeline.process(raw);

        return ResponseEntity.accepted().body("SSIM file accepted");
    }
}
