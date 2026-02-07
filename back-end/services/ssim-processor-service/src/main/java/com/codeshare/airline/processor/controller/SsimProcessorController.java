/*
package com.codeshare.airline.processor.controller;


import com.codeshare.airline.processor.orchestration.SsimProcessingFacade;
import com.codeshare.airline.processor.pipeline.model.SsimRawFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/ssim")
@RequiredArgsConstructor
public class SsimProcessorController {

    private final SsimProcessingFacade coordinator;

    @PostMapping("/raw")
    public ResponseEntity<Void> accept(@RequestBody SsimRawFile rawFile) {
        coordinator.process(rawFile);
        return ResponseEntity.accepted().build();
    }
}

*/
