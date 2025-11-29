package com.codeshare.airline.common.exceptions;


import lombok.Getter;


@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCodes errorCode;

    public BusinessException(ErrorCodes errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCodes errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
}

