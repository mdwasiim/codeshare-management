package com.codeshare.airline.core.dto;

import com.codeshare.airline.core.enums.UserStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserIdentityContext implements Serializable {
    /**
     * JWT subject (user identifier)
     */
    private final String userId;

    /**
     * Tenant to which the user belongs
     */
    private final String tenantId;

    /**
     * Authorization roles (coarse-grained)
     * Example: TENANT_ADMIN, USER
     */
    private final Set<String> roles;

    /**
     * Fine-grained permissions
     * Example: USER_CREATE, TENANT_READ
     */
    private final Set<String> permissions;

    private UserStatus status;
}
