package com.codeshare.airline.auth.authentication.exception;

public class UserAccountLockedException extends RuntimeException {
    public UserAccountLockedException(String message) {
        super(message);
    }
}
