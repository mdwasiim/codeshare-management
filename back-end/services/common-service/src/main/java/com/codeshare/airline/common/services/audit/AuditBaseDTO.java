package com.codeshare.airline.common.services.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AuditBaseDTO   {


    //tenant ID
    private UUID tenantId;

    // Creation
    private LocalDateTime createdAt;

    private String createdBy;

    // Update
    private LocalDateTime updatedAt;

    private String updatedBy;

    // Entity active flag
    private Boolean active;

    // Soft Delete
    private Boolean isDeleted;

    private LocalDateTime deletedAt;

    private String deletedBy;

    // Request trace
    private String transactionId;
}
