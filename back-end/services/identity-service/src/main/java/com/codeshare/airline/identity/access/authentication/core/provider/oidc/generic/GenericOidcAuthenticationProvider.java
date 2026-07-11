package com.codeshare.airline.identity.access.authentication.core.provider.oidc.generic;

import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import com.codeshare.airline.identity.access.assignments.service.RolePermissionAssignmentService;
import com.codeshare.airline.identity.access.authentication.core.domain.OidcAuthenticatedUser;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.identity.access.authentication.core.provider.oidc.base.AbstractOidcAuthenticationProvider;
import com.codeshare.airline.identity.access.authentication.core.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.identity.access.authentication.core.service.core.AuthenticationResult;
import com.codeshare.airline.identity.access.identity.service.AuthUserService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class GenericOidcAuthenticationProvider extends AbstractOidcAuthenticationProvider {

    public GenericOidcAuthenticationProvider(
            GenericOidcClientAdapter oidcClientAdapter,
            RolePermissionAssignmentService rolePermissionAssignmentService,
            AuthUserService authUserService
    ) {
        super(oidcClientAdapter, rolePermissionAssignmentService, authUserService);
    }

    @Override
    public AuthSource getAuthSource() {
        return AuthSource.OIDC_GENERIC;
    }

    @Override
    protected AuthenticationResult mapToAuthResult(
            OidcAuthenticatedUser oidcUser,
            UserDetailsAdapter user,
            TenantContext tenant,
            Set<String> roles,
            Set<String> permissions
    ) {
        return AuthenticationResult.builder()
                .userId(user.getUserId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .externalId(oidcUser.getExternalId())
                .tenantId(tenant.getId())
                .tenantCode(tenant.getTenantCode())
                .userGroups(Set.of())
                .roles(roles)
                .permissions(permissions)
                .authSource(AuthSource.OIDC_GENERIC)
                .build();
    }
}
