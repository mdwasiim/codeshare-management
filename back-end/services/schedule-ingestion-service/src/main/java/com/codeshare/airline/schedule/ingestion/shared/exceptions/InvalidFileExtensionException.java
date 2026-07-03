package com.codeshare.airline.schedule.ingestion.shared.exceptions;

public class InvalidFileExtensionException extends RuntimeException {

    public InvalidFileExtensionException(String message) {
        super(message);
    }

    public InvalidFileExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
}