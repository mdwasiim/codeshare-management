package com.codeshare.airline.auth.authentication.api.request;

import com.codeshare.airline.auth.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.TenantContext;
import com.codeshare.airline.core.enums.AuthSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String username;
    private String password;
    private String authorizationCode;
    // 🔐 PKCE
    private String codeVerifier;

    private String state;

    private AuthSource requestedAuthSource;
    private TenantContext tenant;

    private IdentityProviderConfig identityProviderConfig;


    public boolean isAuthorizationCodeFlow() {
        return authorizationCode != null && !authorizationCode.isBlank();
    }

    public boolean isPasswordFlow() {
        return username != null && password != null;
    }

    public LoginRequest withTenantAndProvider(TenantContext tenant, IdentityProviderConfig idpConfig) {
        return LoginRequest.builder()
                .username(this.username)
                .password(this.password)
                .authorizationCode(this.authorizationCode)
                .tenant(tenant)
                .requestedAuthSource(idpConfig.getAuthSource())
                .identityProviderConfig(idpConfig)
                .build();
    }

}
