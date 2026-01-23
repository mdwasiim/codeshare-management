package com.codeshare.airline.auth.authentication.exception;

public class TenantResolutionException extends RuntimeException {
    public TenantResolutionException(String message) {
        super(message);
    }
}
