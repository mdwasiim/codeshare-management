package com.codeshare.airline.identity.exception;

import com.codeshare.airlineauthentication.exception.*;
import com.codeshare.airline.core.exceptions.CSMErrorCodes;
import com.codeshare.airline.core.response.CSMServiceError;
import com.codeshare.airline.core.response.CSMServiceResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    private static final String CORRELATION_HEADER = "X-Correlation-Id";

    private String getOrGenerateCorrelationId(HttpServletRequest request) {
        String id = request.getHeader(CORRELATION_HEADER);
        return (id == null || id.isBlank())
                ? UUID.randomUUID().toString()
                : id;
    }

    private ResponseEntity<CSMServiceResponse<?>> build(
            HttpStatus status,
            CSMErrorCodes code,
            String message,
            String details,
            Exception ex,
            HttpServletRequest request,
            boolean logStack
    ) {
        String correlationId = getOrGenerateCorrelationId(request);

        CSMServiceError error = CSMServiceError.builder()
                .code(code.getCode())
                .message(message)
                .details(details)
                .build();

        if (logStack) {
            log.error("[{}] {} - {}", correlationId, code.getCode(), message, ex);
        } else {
            log.warn("[{}] {} - {}", correlationId, code.getCode(), message);
        }

        return ResponseEntity.status(status)
                .header(CORRELATION_HEADER, correlationId)
                .body(CSMServiceResponse.error(error));
    }

    /* ============================
       AUTH DOMAIN EXCEPTIONS
       ============================ */

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<CSMServiceResponse<?>> invalidCredentials(
            AuthenticationFailedException ex,
            HttpServletRequest req
    ) {
        return build(
                HttpStatus.UNAUTHORIZED,
                CSMErrorCodes.AUTH_INVALID_CREDENTIALS,
                "Invalid username or password",
                null,
                ex,
                req,
                false
        );
    }

    @ExceptionHandler(UserAccountLockedException.class)
    public ResponseEntity<CSMServiceResponse<?>> userLocked(
            UserAccountLockedException ex,
            HttpServletRequest req
    ) {
        return build(
                HttpStatus.LOCKED,
                CSMErrorCodes.AUTH_USER_LOCKED,
                "User account is locked",
                null,
                ex,
                req,
                false
        );
    }

    @ExceptionHandler(TenantResolutionException.class)
    public ResponseEntity<CSMServiceResponse<?>> tenantNotFound(
            TenantResolutionException ex,
            HttpServletRequest req
    ) {
        return build(
                HttpStatus.FORBIDDEN,
                CSMErrorCodes.AUTH_TENANT_NOT_FOUND,
                ex.getMessage(),
                null,
                ex,
                req,
                false
        );
    }

    @ExceptionHandler(UnsupportedAuthenticationFlowException.class)
    public ResponseEntity<CSMServiceResponse<?>> authFlowNotSupported(
            UnsupportedAuthenticationFlowException ex,
            HttpServletRequest req
    ) {
        return build(
                HttpStatus.BAD_REQUEST,
                CSMErrorCodes.AUTH_FLOW_NOT_SUPPORTED,
                ex.getMessage(),
                null,
                ex,
                req,
                false
        );
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<CSMServiceResponse<?>> refreshTokenInvalid(
            RefreshTokenInvalidException ex,
            HttpServletRequest req
    ) {
        return build(
                HttpStatus.UNAUTHORIZED,
                CSMErrorCodes.AUTH_REFRESH_TOKEN_INVALID,
                "Refresh token is invalid or expired",
                null,
                ex,
                req,
                false
        );
    }

    /* ============================
       PLATFORM / VALIDATION ERRORS
       ============================ */

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            MissingPathVariableException.class
    })
    public ResponseEntity<CSMServiceResponse<?>> validationErrors(
            Exception ex,
            HttpServletRequest req
    ) {
        String message = switch (ex) {
            case MethodArgumentNotValidException e ->
                    e.getBindingResult()
                            .getFieldErrors()
                            .stream()
                            .map(f -> f.getField() + ": " + f.getDefaultMessage())
                            .findFirst()
                            .orElse("Validation failed");

            case ConstraintViolationException e ->
                    e.getConstraintViolations()
                            .stream()
                            .findFirst()
                            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                            .orElse("Validation failed");

            default -> "Invalid request";
        };

        return build(
                HttpStatus.BAD_REQUEST,
                CSMErrorCodes.VALIDATION_ERROR,
                message,
                null,
                ex,
                req,
                false
        );
    }
}

