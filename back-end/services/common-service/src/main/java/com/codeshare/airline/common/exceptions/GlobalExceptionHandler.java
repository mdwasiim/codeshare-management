package com.codeshare.airline.common.exceptions;

import com.codeshare.airline.common.response.common.ServiceError;
import com.codeshare.airline.common.response.common.ServiceResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String CORRELATION_HEADER = "X-Correlation-Id";

    private String getOrGenerateCorrelationId(HttpServletRequest request) {
        String id = request.getHeader(CORRELATION_HEADER);
        return (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
    }

    private ResponseEntity<ServiceResponse<?>> build(
            HttpStatus status,
            ErrorCodes code,
            String message,
            String details,
            Exception ex,
            HttpServletRequest request,
            boolean logStack
    ) {

        String correlationId = getOrGenerateCorrelationId(request);

        ServiceError error = ServiceError.builder()
                .code(code.getCode())
                .message(message)
                .details(details)
                .build();

        if (logStack) log.error("[{}] {} - {}", correlationId, code.getCode(), message, ex);
        else log.error("[{}] {} - {}", correlationId, code.getCode(), message);

        return ResponseEntity.status(status)
                .header(CORRELATION_HEADER, correlationId)
                .body(ServiceResponse.error(error));
    }

    // -------------------------------------------------------
    // BUSINESS EXCEPTION
    // -------------------------------------------------------
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ServiceResponse<?>> handleBusiness(BusinessException ex, HttpServletRequest req) {
        return build(
                HttpStatus.BAD_REQUEST,
                ex.getErrorCode(),
                ex.getMessage(),
                null,
                ex,
                req,
                false
        );
    }

    // -------------------------------------------------------
    // VALIDATION ERROR (@Valid)
    // -------------------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServiceResponse<?>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse(ErrorCodes.VALIDATION_ERROR.getDefaultMessage());

        return build(
                HttpStatus.BAD_REQUEST,
                ErrorCodes.VALIDATION_ERROR,
                message,
                null,
                ex,
                req,
                false
        );
    }

    // -------------------------------------------------------
    // ConstraintViolation
    // -------------------------------------------------------
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ServiceResponse<?>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {

        String message = ex.getConstraintViolations().stream()
                .map(err -> err.getPropertyPath() + ": " + err.getMessage())
                .findFirst()
                .orElse(ErrorCodes.CONSTRAINT_VIOLATION.getDefaultMessage());

        return build(
                HttpStatus.BAD_REQUEST,
                ErrorCodes.CONSTRAINT_VIOLATION,
                message,
                null,
                ex,
                req,
                false
        );
    }

    // -------------------------------------------------------
    // Type Mismatch
    // -------------------------------------------------------
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ServiceResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {

        String msg = "Invalid value for: " + ex.getName();

        return build(
                HttpStatus.BAD_REQUEST,
                ErrorCodes.TYPE_MISMATCH,
                msg,
                "Expected: " + ex.getRequiredType(),
                ex,
                req,
                false
        );
    }

    // -------------------------------------------------------
    // Missing parameters
    // -------------------------------------------------------
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ServiceResponse<?>> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {

        String msg = "Missing parameter: " + ex.getParameterName();

        return build(
                HttpStatus.BAD_REQUEST,
                ErrorCodes.MISSING_PARAMETER,
                msg,
                null,
                ex,
                req,
                false
        );
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ServiceResponse<?>> handleMissingPath(MissingPathVariableException ex, HttpServletRequest req) {

        return build(
                HttpStatus.BAD_REQUEST,
                ErrorCodes.MISSING_PATH_VARIABLE,
                "Missing path variable: " + ex.getVariableName(),
                null,
                ex,
                req,
                false
        );
    }

    // -------------------------------------------------------
    // HTTP Method Not Allowed
    // -------------------------------------------------------
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ServiceResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {

        return build(
                HttpStatus.METHOD_NOT_ALLOWED,
                ErrorCodes.METHOD_NOT_ALLOWED,
                "Method not allowed: " + ex.getMethod(),
                null,
                ex,
                req,
                false
        );
    }

    // -------------------------------------------------------
    // Database exceptions
    // -------------------------------------------------------
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ServiceResponse<?>> handleDb(DataIntegrityViolationException ex, HttpServletRequest req) {

        return build(
                HttpStatus.CONFLICT,
                ErrorCodes.DB_CONSTRAINT_VIOLATION,
                ErrorCodes.DB_CONSTRAINT_VIOLATION.getDefaultMessage(),
                ex.getMostSpecificCause().getMessage(),
                ex,
                req,
                true
        );
    }

    // -------------------------------------------------------
    // NullPointer
    // -------------------------------------------------------
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ServiceResponse<?>> handleNpe(NullPointerException ex, HttpServletRequest req) {

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCodes.NULL_POINTER,
                ErrorCodes.NULL_POINTER.getDefaultMessage(),
                null,
                ex,
                req,
                true
        );
    }

    // -------------------------------------------------------
    // RuntimeException
    // -------------------------------------------------------
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ServiceResponse<?>> handleRuntime(RuntimeException ex, HttpServletRequest req) {

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCodes.RUNTIME_ERROR,
                ErrorCodes.RUNTIME_ERROR.getDefaultMessage(),
                null,
                ex,
                req,
                true
        );
    }
    // -------------------------------------------------------
    // Resource Not Found Exception
    // -------------------------------------------------------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ServiceResponse<?>> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest req
    ) {
        return build(
                HttpStatus.NOT_FOUND,
                ErrorCodes.DATA_NOT_FOUND,   // or your custom code
                ex.getMessage(),
                null,
                ex,
                req,
                false
        );
    }

    // -------------------------------------------------------
    // Generic Exception
    // -------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceResponse<?>> handleGeneric(Exception ex, HttpServletRequest req) {

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCodes.INTERNAL_SERVER_ERROR,
                ErrorCodes.INTERNAL_SERVER_ERROR.getDefaultMessage(),
                null,
                ex,
                req,
                true
        );
    }
}
