package com.codeshare.airline.inbound.common.exceptions;

public class InvalidFileExtensionException extends RuntimeException {

    public InvalidFileExtensionException(String message) {
        super(message);
    }

    public InvalidFileExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
}