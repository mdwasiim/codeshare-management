package com.codeshare.airline.processor.processing.validation;


import com.codeshare.airline.processor.pipeline.model.SsimLoadContext;

public interface SsimValidationService {

    void validate(SsimLoadContext context);
}
