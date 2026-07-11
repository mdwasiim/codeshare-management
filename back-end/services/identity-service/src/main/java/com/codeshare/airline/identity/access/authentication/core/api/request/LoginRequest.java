package com.codeshare.airline.identity.access.authentication.core.api.request;

import com.codeshare.airline.identity.access.authentication.core.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.platform.core.enums.auth.AuthSource;
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
    private String redirectUri;

    private String state;

    private AuthSource authSource;
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
                .codeVerifier(this.codeVerifier)
                .redirectUri(this.redirectUri)
                .tenant(tenant)
                .authSource(idpConfig.getAuthSource())
                .identityProviderConfig(idpConfig)
                .build();
    }

}
