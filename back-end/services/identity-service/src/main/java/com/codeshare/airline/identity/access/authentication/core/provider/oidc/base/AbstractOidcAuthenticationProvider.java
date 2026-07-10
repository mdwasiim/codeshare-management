package com.codeshare.airline.identity.access.authentication.core.provider.oidc.base;

import com.codeshare.airline.identity.access.authentication.core.api.request.LoginRequest;
import com.codeshare.airline.identity.access.authentication.core.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.access.authentication.core.domain.OidcAuthenticatedUser;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.identity.access.authentication.core.exception.AuthenticationFailedException;
import com.codeshare.airline.identity.access.authentication.core.exception.UnsupportedAuthenticationFlowException;
import com.codeshare.airline.identity.access.authentication.core.provider.AuthenticationProvider;
import com.codeshare.airline.identity.access.authentication.core.provider.AuthorizationRedirectCapable;
import com.codeshare.airline.identity.access.authentication.core.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.identity.access.authentication.core.service.core.AuthenticationResult;
import com.codeshare.airline.identity.access.assignments.service.RolePermissionAssignmentService;
import com.codeshare.airline.identity.access.identity.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Set;

@RequiredArgsConstructor
public abstract class AbstractOidcAuthenticationProvider implements AuthenticationProvider,  AuthorizationRedirectCapable {

    protected final OidcClientAdapter oidcClientAdapter;
    protected final RolePermissionAssignmentService rolePermissionAssignmentService;
    protected final AuthUserService authUserService;

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
                        request.getCodeVerifier(),
                        request.getRedirectUri(),
                        idpConfig
                );

        UserDetailsAdapter user = authUserService.getAuthUserForFederatedLogin(
                tenant.getTenantCode(),
                getAuthSource(),
                oidcUser.getExternalId(),
                oidcUser.getUsername(),
                oidcUser.getEmail()
        );

        Set<String> roles = rolePermissionAssignmentService.resolveRoleCodes(user.getUserId());
        Set<String> permissions = rolePermissionAssignmentService.resolvePermissionCodes(user.getUserId());

        return mapToAuthResult(oidcUser, user, tenant, roles, permissions);
    }

    /* ================================
       EXTENSION POINT (CORRECT)
       ================================ */
    protected abstract AuthenticationResult mapToAuthResult(
            OidcAuthenticatedUser oidcUser,
            UserDetailsAdapter user,
            TenantContext tenant,
            Set<String> roles,
            Set<String> permissions
    );

    @Override
    public String buildAuthorizeUrl(
            TenantContext tenant,
            IdentityProviderConfig config,
            String callbackUri,
            String state,
            String codeChallenge,
            String nonce
    ) {
        String frontendRedirectUri = config.getOidcConfig().getRedirectUri();
        if (!StringUtils.hasText(frontendRedirectUri)) {
            throw new AuthenticationFailedException("Frontend redirect URI missing for " + config.getAuthSource());
        }

        return org.springframework.web.util.UriComponentsBuilder
                .fromUriString(config.getOidcConfig().getAuthorizationUri())
                .queryParam("response_type", "code")
                .queryParam("client_id", config.getOidcConfig().getClientId())
                .queryParam("redirect_uri", callbackUri)
                .queryParam("scope", config.getOidcConfig().getScopes())
                .queryParam("state", state)
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", "S256")
                .queryParam("nonce", nonce)
                .queryParam("response_mode", "query")
                .build(true)
                .toUriString();
    }
}
