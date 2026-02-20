package com.codeshare.airline.auth.authentication.service.core;


import com.codeshare.airline.core.enums.auth.AuthSource;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AuthenticationResult {

    /** Normalized username used internally */
    private final String username;

    /** Tenant boundary */
    private final UUID tenantId;

    /** Tenant code */
    private final String tenantCode;

    /** External ID from identity provider */
    private final String externalId;

    /** AZURE / LDAP / LOCAL */
    private final AuthSource authSource;

    /** Application roles */
    private final Set<String> roles;

    /** Application permissions */
    private final Set<String> permissions;

}
