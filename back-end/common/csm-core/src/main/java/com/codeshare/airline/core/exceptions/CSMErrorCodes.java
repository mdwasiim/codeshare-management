package com.codeshare.airline.core.exceptions;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum CSMErrorCodes {

    /* ==========================
       BUSINESS / DOMAIN (1xxx)
       ========================== */
    USER_NOT_FOUND(1001, "USER_NOT_FOUND", "User not found"),
    TENANT_NOT_ACTIVE(1002, "TENANT_NOT_ACTIVE", "Tenant is not active"),
    DUPLICATE_ENTITY(1003, "DUPLICATE_ENTITY", "Record already exists"),
    DATA_NOT_FOUND(1004, "DATA_NOT_FOUND", "Requested data not found"),

    /* ==========================
       VALIDATION (3xxx)
       ========================== */
    VALIDATION_ERROR(3001, "VALIDATION_ERROR", "Validation failed"),
    CONSTRAINT_VIOLATION(3002, "CONSTRAINT_VIOLATION", "Invalid request data"),
    TYPE_MISMATCH(3003, "TYPE_MISMATCH", "Invalid data type"),
    MISSING_PARAMETER(3004, "MISSING_PARAMETER", "Missing required parameter"),
    MISSING_PATH_VARIABLE(3005, "MISSING_PATH_VARIABLE", "Missing path variable"),
    METHOD_NOT_ALLOWED(3006, "METHOD_NOT_ALLOWED", "HTTP method not allowed"),
    UNSUPPORTED_MEDIA_TYPE(3007, "UNSUPPORTED_MEDIA_TYPE", "Unsupported media type"),

    /* ==========================
       DATABASE (4xxx)
       ========================== */
    DB_CONSTRAINT_VIOLATION(4001, "DB_CONSTRAINT_VIOLATION", "Database constraint violation"),
    DB_ERROR(4002, "DB_ERROR", "Database error occurred"),

    /* ==========================
       AUTH SERVICE (808xxx)
       ========================== */
    AUTH_INVALID_CREDENTIALS(808101, "AUTH_INVALID_CREDENTIALS", "Invalid username or password"),
    AUTH_USER_LOCKED(808102, "AUTH_USER_LOCKED", "User account is locked"),
    AUTH_TENANT_NOT_FOUND(808103, "AUTH_TENANT_NOT_FOUND", "Tenant configuration not found"),
    AUTH_REFRESH_TOKEN_INVALID(808104, "AUTH_REFRESH_TOKEN_INVALID", "Refresh token invalid"),
    AUTH_FLOW_NOT_SUPPORTED(808105, "AUTH_FLOW_NOT_SUPPORTED", "Authentication flow not supported"),

    /* ==========================
       SYSTEM / TECHNICAL (9xxx)
       ========================== */
    INTERNAL_SERVER_ERROR(9003, "INTERNAL_SERVER_ERROR", "Something went wrong"),
    SERVICE_UNAVAILABLE(9004, "SERVICE_UNAVAILABLE", "Service is temporarily unavailable"),

    /* ==========================
       API / GATEWAY (10xxx)
       ========================== */
    BAD_REQUEST(10001, "BAD_REQUEST", "Invalid or malformed request"),
    INVALID_JSON(10002, "INVALID_JSON", "Malformed or unreadable request body"),
    API_RATE_LIMIT_EXCEEDED(10004, "API_RATE_LIMIT_EXCEEDED", "Rate limit exceeded"),
    API_SERVICE_ERROR(10006, "API_SERVICE_ERROR", "Downstream service error"),
    BAD_GATEWAY(10009, "BAD_GATEWAY", "Invalid response from upstream server");

    private final int errorCode;
    private final String code;
    private final String defaultMessage;

    CSMErrorCodes(int errorCode, String code, String defaultMessage) {
        this.errorCode = errorCode;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public static Optional<CSMErrorCodes> fromCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst();
    }
}
