package com.codeshare.airline.identity.access.authentication.core.provider.ldap;

import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.identity.access.authentication.core.api.request.LoginRequest;
import com.codeshare.airline.identity.access.authentication.core.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.access.authentication.core.domain.OidcConfig;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.identity.access.authentication.core.exception.AuthenticationFailedException;
import com.codeshare.airline.identity.access.authentication.core.provider.AuthenticationProvider;
import com.codeshare.airline.identity.access.authentication.core.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.identity.access.authentication.core.service.core.AuthenticationResult;
import com.codeshare.airline.identity.access.authentication.core.service.source.TenantIdentityProviderSelector;
import com.codeshare.airline.identity.access.identity.service.AuthUserService;
import com.codeshare.airline.identity.access.assignments.service.RolePermissionAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class LdapAuthenticationProvider implements AuthenticationProvider {

    private final LdapTemplate ldapTemplate;
    private final AuthUserService authUserService;
    private final RolePermissionAssignmentService rolePermissionAssignmentService;
    private final TenantIdentityProviderSelector tenantIdentityProviderSelector;

    @Override
    public AuthSource getAuthSource() {
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
            throw new AuthenticationFailedException("LDAP authentication not enabled for ingestion");
        }

        // 🔐 LDAP CONFIG (provider-specific)
        OidcConfig ldapConfig = idpConfig.getOidcConfig();
        if (ldapConfig == null) {
            throw new AuthenticationFailedException("LDAP configuration missing");
        }

        boolean authenticated = ldapTemplate.authenticate(ldapConfig.getScopes(),"(uid=" + LdapEncoder.filterEncode(request.getUsername()) + ")",request.getPassword());

        if (!authenticated) {
            log.warn("LDAP login failed | user={} tenant={}", request.getUsername(), tenant.getTenantCode());
            throw new AuthenticationFailedException("Invalid LDAP credentials");
        }

        //  Tenant-aware user lookup
        UserDetailsAdapter user = authUserService.getAuthUserByUsername(request.getUsername());

        if (user == null || !user.isEnabled()) {
            throw new AuthenticationFailedException("User not active");
        }

        Set<String> roles = rolePermissionAssignmentService.resolveRoleCodes(user.getUserId());

        Set<String> permissions = rolePermissionAssignmentService.resolvePermissionCodes(user.getUserId());

        log.info("LDAP login success | user={} tenant={}", request.getUsername(), tenant.getTenantCode());

        return AuthenticationResult.builder()
                .userId(user.getUserId().toString())   // ✅ IMPORTANT
                .username(user.getUsername())
                .tenantCode(tenant.getTenantCode())
                .tenantId(tenant.getId())
                .roles(roles)
                .permissions(permissions)
                .authSource(AuthSource.LDAP)
                .build();
    }
}
