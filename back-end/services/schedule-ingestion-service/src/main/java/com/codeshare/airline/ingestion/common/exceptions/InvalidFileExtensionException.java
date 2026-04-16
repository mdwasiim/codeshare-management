package com.codeshare.airline.ingestion.common.exceptions;

public class InvalidFileExtensionException extends RuntimeException {

    public InvalidFileExtensionException(String message) {
        super(message);
    }

    public InvalidFileExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
}