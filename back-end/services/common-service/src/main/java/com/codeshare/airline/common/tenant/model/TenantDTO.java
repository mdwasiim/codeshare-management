package com.codeshare.airline.common.tenant.model;

import com.codeshare.airline.common.audit.AuditBaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantDTO extends AuditBaseDto {

    private UUID id;
    private String name;
    private String code;
    private String description;
    private boolean enabled;

    // Optional: DB info (usually not exposed via API)
    private String dbUrl;
    private String dbUsername;

    // Subscription / Plan Info
    private String plan;
    private LocalDateTime subscriptionStart;
    private LocalDateTime subscriptionEnd;
    private boolean trial;

    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    // Optional extra info
    private String contactEmail;
    private String contactPhone;
    private String logoUrl;
    private String region;

}
