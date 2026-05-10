package com.codeshare.airline.identity.authentication.provider.oidc.base;

import com.codeshare.airline.identity.authentication.api.request.LoginRequest;
import com.codeshare.airline.identity.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.authentication.domain.OidcAuthenticatedUser;
import com.codeshare.airline.identity.authentication.domain.TenantContext;
import com.codeshare.airline.identity.authentication.exception.AuthenticationFailedException;
import com.codeshare.airline.identity.authentication.exception.UnsupportedAuthenticationFlowException;
import com.codeshare.airline.identity.authentication.provider.AuthenticationProvider;
import com.codeshare.airline.identity.authentication.provider.AuthorizationRedirectCapable;
import com.codeshare.airline.identity.authentication.service.core.AuthenticationResult;
import com.codeshare.airline.identity.service.assignment.RolePermissionAssignmentService;
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
                    "OIDC authentication requires authorization code coordination"
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
                rolePermissionAssignmentService.resolveRoleCodes(oidcUser.getUserId());
        Set<String> permissions = rolePermissionAssignmentService.resolvePermissionCodes(oidcUser.getUserId());

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
