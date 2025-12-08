package com.codeshare.airline.common.services.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCodes {

    // --- BUSINESS / DOMAIN ERRORS ---
    USER_NOT_FOUND(1001, "USER_NOT_FOUND", "User not found"),
    TENANT_NOT_ACTIVE(1002, "TENANT_NOT_ACTIVE", "Tenant is not active"),
    DUPLICATE_ENTITY(1003, "DUPLICATE_ENTITY", "Record already exists"),
    DATA_NOT_FOUND(1004, "DATA_NOT_FOUND", "Requested data not found"),

    // --- AUTH & SECURITY ---
    INVALID_CREDENTIALS(2001, "INVALID_CREDENTIALS", "Invalid username or password"),
    TOKEN_EXPIRED(2002, "TOKEN_EXPIRED", "Token expired"),
    INVALID_TOKEN(2003, "INVALID_TOKEN", "Invalid token"),
    UNAUTHORIZED(2004, "UNAUTHORIZED", "Authentication required"),
    ACCESS_DENIED(2005, "ACCESS_DENIED", "Access denied"),

    // --- VALIDATION ---
    VALIDATION_ERROR(3001, "VALIDATION_ERROR", "Validation failed"),
    CONSTRAINT_VIOLATION(3002, "CONSTRAINT_VIOLATION", "Invalid request data"),
    TYPE_MISMATCH(3003, "TYPE_MISMATCH", "Invalid data type"),
    MISSING_PARAMETER(3004, "MISSING_PARAMETER", "Missing required parameter"),
    MISSING_PATH_VARIABLE(3005, "MISSING_PATH_VARIABLE", "Missing path variable"),
    METHOD_NOT_ALLOWED(3006, "METHOD_NOT_ALLOWED", "HTTP method not allowed"),
    UNSUPPORTED_MEDIA_TYPE(3007, "UNSUPPORTED_MEDIA_TYPE", "Unsupported media type"),

    // --- DATABASE ---
    DB_CONSTRAINT_VIOLATION(4001, "DB_CONSTRAINT_VIOLATION", "Database constraint violation"),
    DB_ERROR(4002, "DB_ERROR", "Database error occurred"),

    // --- SYSTEM / TECHNICAL ---
    NULL_POINTER(9001, "NULL_POINTER", "A required value was missing"),
    RUNTIME_ERROR(9002, "RUNTIME_ERROR", "Unexpected runtime error"),
    INTERNAL_SERVER_ERROR(9003, "INTERNAL_SERVER_ERROR", "Something went wrong"),
    SERVICE_UNAVAILABLE(9004, "SERVICE_UNAVAILABLE", "Service is temporarily unavailable"),

    // --- API COMMON ---
    BAD_REQUEST(10001, "BAD_REQUEST", "Invalid or malformed request"),
    INVALID_JSON(10002, "INVALID_JSON", "Malformed or unreadable request body"),
    INVALID_API_VERSION(10003, "INVALID_API_VERSION", "Unsupported API version"),
    API_RATE_LIMIT_EXCEEDED(10004, "API_RATE_LIMIT_EXCEEDED", "Rate limit exceeded"),
    API_GATEWAY_TIMEOUT(10005, "API_GATEWAY_TIMEOUT", "Gateway timeout"),
    API_SERVICE_ERROR(10006, "API_SERVICE_ERROR", "Downstream service error"),
    CIRCUIT_BREAKER_OPEN(10007, "CIRCUIT_BREAKER_OPEN", "Service temporarily blocked"),
    API_NOT_AVAILABLE(10008, "API_NOT_AVAILABLE", "API is temporarily unavailable"),
    BAD_GATEWAY(10009, "BAD_GATEWAY", "Invalid response from upstream server");

    private final int errorCode;
    private final String code;
    private final String defaultMessage;

    ErrorCodes(int errorCode, String code, String defaultMessage) {
        this.errorCode = errorCode;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
