package com.codeshare.airline.core.exceptions;


import lombok.Getter;


@Getter
public class CSMBusinessException extends RuntimeException {

    private final CSMErrorCodes errorCode;
    private final String details;

    public CSMBusinessException(CSMErrorCodes errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    public CSMBusinessException(CSMErrorCodes errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
        this.details = null;
    }

    public CSMBusinessException(
            CSMErrorCodes errorCode,
            String customMessage,
            String details
    ) {
        super(customMessage);
        this.errorCode = errorCode;
        this.details = details;
    }

    public CSMBusinessException(
            CSMErrorCodes errorCode,
            Throwable cause
    ) {
        super(errorCode.getDefaultMessage(), cause);
        this.errorCode = errorCode;
        this.details = cause != null ? cause.getMessage() : null;
    }

    public CSMBusinessException(
            CSMErrorCodes errorCode,
            String customMessage,
            Throwable cause
    ) {
        super(customMessage, cause);
        this.errorCode = errorCode;
        this.details = cause != null ? cause.getMessage() : null;
    }
}


