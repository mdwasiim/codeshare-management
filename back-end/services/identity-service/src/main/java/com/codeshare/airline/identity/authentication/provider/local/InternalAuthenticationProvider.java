package com.codeshare.airline.identity.authentication.provider.local;

import com.codeshare.airline.core.enums.auth.AuthSource;
import com.codeshare.airline.identity.authentication.api.request.LoginRequest;
import com.codeshare.airline.identity.authentication.domain.TenantContext;
import com.codeshare.airline.identity.authentication.exception.AuthenticationFailedException;
import com.codeshare.airline.identity.authentication.provider.AuthenticationProvider;
import com.codeshare.airline.identity.authentication.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.identity.authentication.service.core.AuthenticationResult;
import com.codeshare.airline.identity.service.AuthUserService;
import com.codeshare.airline.identity.service.RolePermissionAssignmentService;
import com.codeshare.airline.identity.service.UserGroupAssignmentService;
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
    private final UserGroupAssignmentService userGroupAssignmentService;


    @Override
    public AuthSource getAuthSource() {
        return AuthSource.INTERNAL;
    }

    @Override
    public AuthenticationResult authenticate(LoginRequest loginRequest) {
        TenantContext tenantContext = loginRequest.getTenant();
        UserDetailsAdapter userDetailsAdapter = authUserService.getAuthUserByUsername(loginRequest.getUsername());

        if (userDetailsAdapter == null ||
                !passwordEncoder.matches(loginRequest.getPassword(), userDetailsAdapter.getPassword())) {
            throw new AuthenticationFailedException("Invalid username or password");
        }

        Set<String> userGroups = userGroupAssignmentService.resolveGroupCodes(userDetailsAdapter.getUserId());
        Set<String> roles = rolePermissionAssignmentService.resolveRoleCodes(userDetailsAdapter.getUserId());
        Set<String> permissions = rolePermissionAssignmentService.resolvePermissionCodes(userDetailsAdapter.getUserId());
        return AuthenticationResult.builder()
                .userId(userDetailsAdapter.getUserId().toString())
                .username(userDetailsAdapter.getUsername())
                .email(userDetailsAdapter.getEmail())
                .tenantCode(tenantContext.getTenantCode())
                .tenantId(loginRequest.getTenant().getId())
                .userGroups(userGroups)
                .roles(roles)
                .permissions(permissions)
                .authSource(AuthSource.INTERNAL)
                .build();
    }
}

