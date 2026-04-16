package com.codeshare.airline.core.dto.audit.dto;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class CSMAuditableDTO {

    private UUID id;
    private UUID tenantId;

    private Instant createdAt;

    private String createdBy;

    private Instant updatedAt;
    private String updatedBy;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Boolean deleted = false;

    private Instant deletedAt;
    private String deletedBy;

    private String transactionId;
}