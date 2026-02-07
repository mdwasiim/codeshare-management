package com.codeshare.airline.processor.processing.validation;

import com.codeshare.airline.processor.pipeline.model.SsimRawFile;
import org.springframework.stereotype.Component;

@Component
public class SsimStructuralValidator {

    public void validate(SsimRawFile file) {

        if (file.getAirlineCode() == null) {
            throw new IllegalStateException("Missing airline code");
        }
        if (file.getContent() == null || file.getContent().length == 0) {
            throw new IllegalStateException("Empty SSIM content");
        }
    }
}
