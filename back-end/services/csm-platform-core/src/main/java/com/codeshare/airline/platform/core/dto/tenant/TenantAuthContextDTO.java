package com.codeshare.airline.platform.core.dto.tenant;

import com.codeshare.airline.platform.core.enums.common.TenantStatus;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantAuthContextDTO {

    private UUID id;
    private String name;
    private String tenantCode;
    private TenantStatus status;
    private String logoUrl;
    private String region;
    private List<IdentityProviderConfigDTO> identityProviders;
}
