package com.codeshare.airline.platform.web.exception;

import com.codeshare.airline.platform.core.exceptions.CSMBusinessException;
import com.codeshare.airline.platform.core.exceptions.CSMErrorCodes;
import com.codeshare.airline.platform.core.exceptions.CSMResourceNotFoundException;
import com.codeshare.airline.platform.core.response.CSMServiceError;
import com.codeshare.airline.platform.core.response.CSMServiceResponse;
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
public class GlobalExceptionHandler {

    private static final String CORRELATION_HEADER = "X-Transaction-Id";

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
       BUSINESS EXCEPTIONS
       ============================ */

    @ExceptionHandler(CSMBusinessException.class)
    public ResponseEntity<CSMServiceResponse<?>> handleBusiness(
            CSMBusinessException ex,
            HttpServletRequest req
    ) {
        return build(
                HttpStatus.BAD_REQUEST,
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getDetails(),
                ex,
                req,
                false
        );
    }

    @ExceptionHandler(CSMResourceNotFoundException.class)
    public ResponseEntity<CSMServiceResponse<?>> handleNotFound(
            CSMResourceNotFoundException ex,
            HttpServletRequest req
    ) {
        return build(
                HttpStatus.NOT_FOUND,
                CSMErrorCodes.DATA_NOT_FOUND,
                ex.getMessage(),
                null,
                ex,
                req,
                false
        );
    }

    /* ============================
       VALIDATION ERRORS
       ============================ */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CSMServiceResponse<?>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest req
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CSMServiceResponse<?>> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest req
    ) {
        String message = ex.getConstraintViolations()
                .stream()
                .findFirst()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .orElse("Validation failed");

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

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            MissingPathVariableException.class
    })
    public ResponseEntity<CSMServiceResponse<?>> handleBadRequest(
            Exception ex,
            HttpServletRequest req
    ) {
        return build(
                HttpStatus.BAD_REQUEST,
                CSMErrorCodes.VALIDATION_ERROR,
                "Invalid request",
                ex.getMessage(),
                ex,
                req,
                false
        );
    }

    /* ============================
       FALLBACK
       ============================ */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CSMServiceResponse<?>> fallback(
            Exception ex,
            HttpServletRequest req
    ) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                CSMErrorCodes.INTERNAL_SERVER_ERROR,
                "Something went wrong",
                null,
                ex,
                req,
                true
        );
    }
}