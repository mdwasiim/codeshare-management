package com.codeshare.airline.auth.authentication.provider.local;

import com.codeshare.airline.auth.authentication.api.request.LoginRequest;
import com.codeshare.airline.auth.authentication.domain.TenantContext;
import com.codeshare.airline.auth.authentication.exception.AuthenticationFailedException;
import com.codeshare.airline.auth.authentication.provider.AuthenticationProvider;
import com.codeshare.airline.auth.authentication.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.auth.authentication.service.core.AuthenticationResult;
import com.codeshare.airline.auth.service.AuthUserService;
import com.codeshare.airline.auth.service.RolePermissionAssignmentService;
import com.codeshare.airline.core.enums.AuthSource;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("LOCAL")
@RequiredArgsConstructor
public class InternalAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final AuthUserService authUserService;
    private final RolePermissionAssignmentService rolePermissionAssignmentService;


    @Override
    public AuthSource getAuthSource() {
        return AuthSource.INTERNAL;
    }

    @Override
    public AuthenticationResult authenticate(LoginRequest loginRequest) {
        TenantContext tenantContext = loginRequest.getTenant();
        UserDetailsAdapter userDetailsAdapter = authUserService.getAuthUserByUsername(loginRequest.getUsername());

        if (userDetailsAdapter ==null && !passwordEncoder.matches(loginRequest.getPassword(), userDetailsAdapter.getPassword())) {
            throw new AuthenticationFailedException("Invalid username or password");
        }

        Set<String> rolese = rolePermissionAssignmentService.resolveRoleNames(userDetailsAdapter.getUserId());
        Set<String> permissions = rolePermissionAssignmentService.resolvePermissionsNames(userDetailsAdapter.getUserId());
        return AuthenticationResult.builder()
                .username(userDetailsAdapter.getUsername())
                .tenantCode(tenantContext.getTenantCode())
                .tenantId(loginRequest.getTenant().getId())
                .roles(rolese)
                .permissions(permissions)
                .authSource(AuthSource.INTERNAL)
                .build();
    }
}

