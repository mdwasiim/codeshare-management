package com.codeshare.airline.auth.authentication.provider.ldap;

import com.codeshare.airline.auth.authentication.api.request.LoginRequest;
import com.codeshare.airline.auth.authentication.domain.model.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.model.OidcConfig;
import com.codeshare.airline.auth.authentication.domain.model.TenantContext;
import com.codeshare.airline.auth.authentication.exception.AuthenticationFailedException;
import com.codeshare.airline.auth.authentication.provider.AuthenticationProvider;
import com.codeshare.airline.auth.authentication.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.auth.authentication.service.core.AuthenticationResult;
import com.codeshare.airline.auth.authentication.service.source.TenantIdentityProviderSelector;
import com.codeshare.airline.auth.service.AuthUserService;
import com.codeshare.airline.auth.service.RolePermissionAssignmentService;
import com.codeshare.airline.core.enums.AuthSource;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class LdapAuthenticationProvider implements AuthenticationProvider {

    private final LdapTemplate ldapTemplate;
    private final AuthUserService authUserService;
    private final RolePermissionAssignmentService rolePermissionAssignmentService;
    private final TenantIdentityProviderSelector tenantIdentityProviderSelector;

    @Override
    public AuthSource supports() {
        return AuthSource.LDAP;
    }

    @Override
    public AuthenticationResult authenticate(LoginRequest request) {

        TenantContext tenant = request.getTenant();
        if (tenant == null) {
            throw new AuthenticationFailedException("Tenant context missing");
        }

        IdentityProviderConfig idpConfig = tenantIdentityProviderSelector.select(tenant, AuthSource.LDAP);

        if (!idpConfig.isEnabled() || idpConfig.getAuthSource() != AuthSource.LDAP) {
            throw new AuthenticationFailedException("LDAP authentication not enabled for tenant");
        }

        // 🔐 LDAP CONFIG (provider-specific)
        OidcConfig ldapConfig = idpConfig.getOidcConfig();
        if (ldapConfig == null) {
            throw new AuthenticationFailedException("LDAP configuration missing");
        }

        boolean authenticated = ldapTemplate.authenticate(ldapConfig.getScopes(),"(uid=" + LdapEncoder.filterEncode(request.getUsername()) + ")",request.getPassword());

        if (!authenticated) {
            throw new AuthenticationFailedException("Invalid LDAP credentials");
        }

        // ✅ Tenant-aware user lookup
        UserDetailsAdapter user = authUserService.getAuthUserByUsername(request.getUsername());

        if (user == null || !user.isEnabled()) {
            throw new AuthenticationFailedException("User not active");
        }

        Set<String> roles = rolePermissionAssignmentService.resolveRoleNames(user.getUserId());

        Set<String> permissions = rolePermissionAssignmentService.resolvePermissionsNames(user.getUserId());

        return AuthenticationResult.builder()
                .username(user.getUsername())
                .tenantCode(tenant.getTenantCode())
                .tenantId(tenant.getId())
                .roles(roles)
                .permissions(permissions)
                .authSource(AuthSource.LDAP)
                .build();
    }
}
