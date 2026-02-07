package com.codeshare.airline.processor.exception;

// Retryable → retry topic
public class RetryableProcessingException extends RuntimeException {
    public RetryableProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}