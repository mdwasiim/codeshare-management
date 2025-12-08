package com.codeshare.airline.common.tenant.model;

import com.codeshare.airline.common.services.audit.AuditBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TenantDTO extends AuditBaseDTO {

    private UUID id;

    private String name;
    private String code;
    private String description;

    private Boolean enabled;

    // DB config reference (safe)
    private UUID dataSourceId;

    // Subscription / Plan Info
    private String plan;
    private LocalDateTime subscriptionStart;
    private LocalDateTime subscriptionEnd;
    private Boolean trial;

    // Contact & Branding
    private String contactEmail;
    private String contactPhone;
    private String logoUrl;
    private String region;
}
