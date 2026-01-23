package com.codeshare.airline.auth.authentication.exception;

public class UnsupportedAuthenticationFlowException extends RuntimeException {
    public UnsupportedAuthenticationFlowException(String message) {
        super(message);
    }
}
