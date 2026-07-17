package com.codeshare.airline.identity.access.authentication.core.domain;

import com.codeshare.airline.platform.core.enums.common.TenantStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TenantContext {

    private Long id;

    private String name;

    private String tenantCode;

    private TenantStatus status ;

    private String logoUrl;

    private String region;

    private List<IdentityProviderConfig> identityProviders;
}

