package com.codeshare.airline.identity.access.authentication.core.exception;

public class UserAccountLockedException extends RuntimeException {
    public UserAccountLockedException(String message) {
        super(message);
    }
}
