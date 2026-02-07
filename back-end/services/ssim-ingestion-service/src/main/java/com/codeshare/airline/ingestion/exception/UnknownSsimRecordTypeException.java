package com.codeshare.airline.ingestion.exception;

public class UnknownSsimRecordTypeException extends RuntimeException {

    public UnknownSsimRecordTypeException(char type, String line) {
        super("Unknown SSIM record type [" + type + "] in line: " + line);
    }
}
