package com.codeshare.airline.platform.core.dto.tenant;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import com.codeshare.airline.platform.core.enums.common.TenantStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TenantDTO extends CSMAuditableDTO {

    private Long id;

    private String name;
    private String tenantCode;
    private String description;

    // DB config reference (safe)
    private Long databaseConfigId;

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
    private AuthSource authSource;
    private OidcConfigDTO oidcConfig;

}
