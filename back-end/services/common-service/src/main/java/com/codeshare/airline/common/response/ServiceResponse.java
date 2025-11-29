package com.codeshare.airline.common.response;

import com.codeshare.airline.common.httpTransaction.TransactionIdProvider;
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
    private String transactionId;
    private LocalDateTime timestamp;

    public static <T> ServiceResponse<T> success(T data) {
        return ServiceResponse.<T>builder()
                .success(true)
                .data(data)
                .transactionId(TransactionIdProvider.get())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ServiceResponse<T> error(ServiceError error) {
        return ServiceResponse.<T>builder()
                .success(false)
                .error(error)
                .transactionId(TransactionIdProvider.get())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
