package com.codeshare.airline.processor.exception;

// Non-retryable → DLT
public class NonRetryableProcessingException extends RuntimeException {
    public NonRetryableProcessingException(String msg) {
        super(msg);
    }
}