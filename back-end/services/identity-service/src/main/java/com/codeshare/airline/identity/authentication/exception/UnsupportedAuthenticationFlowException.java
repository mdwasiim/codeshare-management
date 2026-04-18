package com.codeshare.airline.identity.authentication.exception;

public class UnsupportedAuthenticationFlowException extends RuntimeException {
    public UnsupportedAuthenticationFlowException(String message) {
        super(message);
    }
}
