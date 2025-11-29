package com.codeshare.airline.common.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResponse<T> {

    private boolean success;
    private T data;
    private ServiceError error;
    private LocalDateTime timestamp;

    public static <T> ServiceResponse<T> success(T data) {
        return ServiceResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ServiceResponse<T> success(T data, String message) {
        return ServiceResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ServiceResponse<T> error(ServiceError error) {
        return ServiceResponse.<T>builder()
                .success(false)
                .error(error)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
