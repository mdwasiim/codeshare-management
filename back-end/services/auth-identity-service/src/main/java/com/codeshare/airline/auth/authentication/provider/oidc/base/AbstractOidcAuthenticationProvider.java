package com.codeshare.airline.auth.authentication.provider.oidc.base;

import com.codeshare.airline.auth.authentication.api.request.LoginRequest;
import com.codeshare.airline.auth.authentication.domain.model.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.model.OidcAuthenticatedUser;
import com.codeshare.airline.auth.authentication.domain.model.TenantContext;
import com.codeshare.airline.auth.authentication.exception.AuthenticationFailedException;
import com.codeshare.airline.auth.authentication.exception.UnsupportedAuthenticationFlowException;
import com.codeshare.airline.auth.authentication.provider.AuthenticationProvider;
import com.codeshare.airline.auth.authentication.provider.AuthorizationRedirectCapable;
import com.codeshare.airline.auth.authentication.service.core.AuthenticationResult;
import com.codeshare.airline.auth.service.RolePermissionAssignmentService;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public abstract class AbstractOidcAuthenticationProvider implements AuthenticationProvider,  AuthorizationRedirectCapable {

    protected final OidcClientAdapter oidcClientAdapter;
    protected final RolePermissionAssignmentService rolePermissionAssignmentService;

    @Override
    public final AuthenticationResult authenticate(LoginRequest request) {

        if (!request.isAuthorizationCodeFlow()) {
            throw new UnsupportedAuthenticationFlowException(
                    "OIDC authentication requires authorization code flow"
            );
        }


        TenantContext tenant = request.getTenant();
        if (tenant == null) {
            throw new AuthenticationFailedException("Tenant context missing");
        }
        IdentityProviderConfig idpConfig = request.getIdentityProviderConfig();

        OidcAuthenticatedUser oidcUser =
                oidcClientAdapter.exchangeCodeForUser(
                        request.getAuthorizationCode(),
                        idpConfig
                );

        Set<String> roles =
                rolePermissionAssignmentService.resolveRoleNames(oidcUser.getUserId());
        Set<String> permissions = rolePermissionAssignmentService.resolvePermissionsNames(oidcUser.getUserId());

        return mapToAuthResult(oidcUser, tenant, roles, permissions);
    }

    /* ================================
       EXTENSION POINT (CORRECT)
       ================================ */
    protected abstract AuthenticationResult mapToAuthResult(
            OidcAuthenticatedUser oidcUser,
            TenantContext tenant,
            Set<String> roles,
            Set<String> permissions
    );
}
