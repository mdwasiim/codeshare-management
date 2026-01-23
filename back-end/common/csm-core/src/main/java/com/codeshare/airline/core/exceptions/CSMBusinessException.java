package com.codeshare.airline.core.exceptions;


import lombok.Getter;


@Getter
public class CSMBusinessException extends RuntimeException {

    private final CSMErrorCodes errorCode;

    public CSMBusinessException(CSMErrorCodes errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public CSMBusinessException(CSMErrorCodes errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}

