package com.codeshare.airline.core.dto.audit.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class CSMAuditableDTO implements Serializable {


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
