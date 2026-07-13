package com.codeshare.airline.platform.core.dto.tenant;

import com.codeshare.airline.platform.core.enums.common.TenantStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantAuthContextDTO {

    private Long id;
    private String name;
    private String tenantCode;
    private TenantStatus status;
    private String logoUrl;
    private String region;
    private List<IdentityProviderConfigDTO> identityProviders;
}
