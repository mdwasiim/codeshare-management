package com.codeshare.airline.ssim.source.rest;


import com.codeshare.airline.ssim.processor.SsimProcessor;
import com.codeshare.airline.ssim.source.SsimSourceFile;
import com.codeshare.airline.ssim.source.SsimSourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/ssim")
@RequiredArgsConstructor
public class SsimUploadController {

    private final SsimProcessor processor;

    @PostMapping("/upload")
    public UploadResponse upload(@RequestParam MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Empty SSIM file");
        }

        SsimSourceFile sourceFile = SsimSourceFile.builder()
                .fileId(UUID.randomUUID().toString())
                .fileName(file.getOriginalFilename())
                .sourceType(SsimSourceType.REST)
                .sourceSystem("HTTP_UPLOAD")
                .receivedAt(Instant.now())
                .streamSupplier(() -> {
                    try {
                        return file.getInputStream();
                    } catch (IOException e) {
                        throw new IllegalStateException("Failed to open SSIM input stream", e);
                    }
                }).build();

        processor.process(sourceFile);

        return UploadResponse.accepted(sourceFile.getFileId());
    }


}
