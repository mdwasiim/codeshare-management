package com.codeshare.airline.auth.authentication.domain.model;

import com.codeshare.airline.core.enums.common.TenantStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class TenantContext {

    private UUID id;

    private String name;

    private String tenantCode;

    private TenantStatus status ;

    private String logoUrl;

    private String region;

    private List<IdentityProviderConfig> identityProviders;
}

