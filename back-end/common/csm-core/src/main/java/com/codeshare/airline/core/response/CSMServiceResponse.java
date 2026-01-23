package com.codeshare.airline.core.response;

import com.codeshare.airline.core.utils.CSMTransactionIdProvider;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CSMServiceResponse<T> {

    private boolean success;
    private T data;
    private CSMServiceError error;
    private String transactionId;
    private LocalDateTime timestamp;
    private long timeTakenMs;

    public static <T> CSMServiceResponse<T> success(T data) {
        return CSMServiceResponse.<T>builder()
                .success(true)
                .data(data)
                .transactionId(CSMTransactionIdProvider.get())
                .timestamp(LocalDateTime.now())
                .timeTakenMs(CSMRequestTimeProvider.getTimeTaken())
                .build();
    }

    public static <T> CSMServiceResponse<T> error(CSMServiceError error) {
        return CSMServiceResponse.<T>builder()
                .success(false)
                .error(error)
                .transactionId(CSMTransactionIdProvider.get())
                .timestamp(LocalDateTime.now())
                .timeTakenMs(CSMRequestTimeProvider.getTimeTaken())
                .build();
    }
}
