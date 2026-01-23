package com.codeshare.airline.auth.authentication.provider.oidc.azure;

import com.codeshare.airline.auth.authentication.domain.model.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.model.OidcAuthenticatedUser;
import com.codeshare.airline.auth.authentication.domain.model.TenantContext;
import com.codeshare.airline.auth.authentication.provider.oidc.base.AbstractOidcAuthenticationProvider;
import com.codeshare.airline.auth.authentication.provider.oidc.base.OidcClientAdapter;
import com.codeshare.airline.auth.authentication.provider.oidc.base.OidcStateManager;
import com.codeshare.airline.auth.authentication.service.core.AuthenticationResult;
import com.codeshare.airline.auth.service.RolePermissionAssignmentService;
import com.codeshare.airline.core.enums.AuthSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@Component
public class AzureOidcAuthenticationProvider extends AbstractOidcAuthenticationProvider {

    @Autowired
    private OidcStateManager oidcStateManager;

    public AzureOidcAuthenticationProvider(OidcClientAdapter oidcClientAdapter, RolePermissionAssignmentService rolePermissionAssignmentService) {
        super(oidcClientAdapter, rolePermissionAssignmentService);
    }


    public AuthSource supports() {
        return AuthSource.AZURE;
    }

    @Override
    protected AuthenticationResult mapToAuthResult(
            OidcAuthenticatedUser oidcUser,
            TenantContext tenant,
            Set<String> roles,
            Set<String>  permissions
    ) {
        return AuthenticationResult.builder()
                .username(oidcUser.getUsername())
                .externalId(oidcUser.getExternalId())
                .tenantId(tenant.getId())
                .tenantCode(tenant.getTenantCode())
                .roles(roles)
                .permissions(permissions)
                .authSource(AuthSource.AZURE)
                .build();
    }

    @Override
    public String buildAuthorizeUrl(
            TenantContext tenant,
            IdentityProviderConfig config,
            String state,
            String codeChallenge,
            String nonce
    ) {

        return UriComponentsBuilder
                .fromUriString(
                        config.getOidcConfig().getAuthorizationUri()
                )
                .queryParam("response_type", "code")
                .queryParam(
                        "client_id",
                        config.getOidcConfig().getClientId()
                )
                .queryParam(
                        "redirect_uri",
                        config.getOidcConfig().getRedirectUri()
                )
                .queryParam(
                        "scope",
                        config.getOidcConfig().getScopes()
                )
                // 🔐 CSRF protection
                .queryParam("state", state)
                // 🔐 PKCE (MANDATORY)
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", "S256")
                // 🔐 OIDC replay protection
                .queryParam("nonce", nonce)
                // Azure best practice
                .queryParam("response_mode", "query")
                .build(true)
                .toUriString();
    }




}



