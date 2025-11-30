package com.codeshare.airline.common.response.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceError {
    private String code;     // e.g. USER_NOT_FOUND, VALIDATION_ERROR
    private String message;  // Human readable message
    private String details;  // Optional technical detail or stack info
}
