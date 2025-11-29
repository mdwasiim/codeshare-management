package com.codeshare.airline.common.exceptions;


import lombok.Getter;

@Getter
public enum ErrorCodes {

    // --- BUSINESS / DOMAIN ERRORS ---
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found"),
    TENANT_NOT_ACTIVE("TENANT_NOT_ACTIVE", "Tenant is not active"),
    DUPLICATE_ENTITY("DUPLICATE_ENTITY", "Record already exists"),
    DATA_NOT_FOUND("DATA_NOT_FOUND", "Requested data not found"),

    // --- AUTH & SECURITY ---
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Invalid username or password"),
    TOKEN_EXPIRED("TOKEN_EXPIRED", "Token expired"),
    INVALID_TOKEN("INVALID_TOKEN", "Invalid token"),
    UNAUTHORIZED("UNAUTHORIZED", "Authentication required"),
    ACCESS_DENIED("ACCESS_DENIED", "Access denied"),

    // --- VALIDATION ---
    VALIDATION_ERROR("VALIDATION_ERROR", "Validation failed"),
    CONSTRAINT_VIOLATION("CONSTRAINT_VIOLATION", "Invalid request data"),

    // --- REQUEST ISSUES ---
    TYPE_MISMATCH("TYPE_MISMATCH", "Invalid data type"),
    MISSING_PARAMETER("MISSING_PARAMETER", "Missing required parameter"),
    MISSING_PATH_VARIABLE("MISSING_PATH_VARIABLE", "Missing path variable"),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "HTTP method not allowed"),
    UNSUPPORTED_MEDIA_TYPE("UNSUPPORTED_MEDIA_TYPE", "Unsupported media type"),

    // --- DATABASE ---
    DB_CONSTRAINT_VIOLATION("DB_CONSTRAINT_VIOLATION", "Database constraint violation"),
    DB_ERROR("DB_ERROR", "Database error occurred"),

    // --- SYSTEM / TECHNICAL ---
    NULL_POINTER("NULL_POINTER", "A required value was missing"),
    RUNTIME_ERROR("RUNTIME_ERROR", "Unexpected runtime error"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Something went wrong"),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "Service is temporarily unavailable");

    private final String code;
    private final String defaultMessage;

    ErrorCodes(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
