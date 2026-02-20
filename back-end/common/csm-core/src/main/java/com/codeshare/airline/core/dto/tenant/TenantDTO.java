package com.codeshare.airline.core.dto.tenant;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import com.codeshare.airline.core.enums.common.TenantStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TenantDTO extends CSMAuditableDTO {

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

    private TenantStatus status = TenantStatus.ACTIVE;


    public boolean canAuthenticate() {
        return enabled && subscriptionEnd == null || subscriptionEnd.isAfter(LocalDateTime.now());
    }
}
