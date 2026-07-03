package com.codeshare.airline.identity.access.authentication.core.exception;

public class TenantResolutionException extends RuntimeException {
    public TenantResolutionException(String message) {
        super(message);
    }
}
