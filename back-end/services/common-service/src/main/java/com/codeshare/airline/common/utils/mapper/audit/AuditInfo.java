package com.codeshare.airline.common.utils.mapper.audit;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditInfo {

    private String createdBy;
    private String updatedBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String transactionId;

    // Soft Delete Info
    private Boolean deleted;
    private LocalDateTime deletedAt;
    private String deletedBy;
}
