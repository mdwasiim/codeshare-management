package com.codeshare.airline.auth.controller.api;

import com.codeshare.airline.auth.authentication.api.request.LoginRequest;
import com.codeshare.airline.auth.authentication.api.request.OidcTokenExchangeRequest;
import com.codeshare.airline.auth.authentication.api.response.LoginResponse;
import com.codeshare.airline.auth.authentication.api.response.RefreshTokenResponse;
import com.codeshare.airline.auth.authentication.domain.model.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.model.TenantContext;
import com.codeshare.airline.auth.authentication.domain.model.TokenPair;
import com.codeshare.airline.auth.authentication.exception.AuthenticationFailedException;
import com.codeshare.airline.auth.authentication.exception.UnsupportedAuthenticationFlowException;
import com.codeshare.airline.auth.authentication.provider.AuthenticationProvider;
import com.codeshare.airline.auth.authentication.provider.AuthorizationRedirectCapable;
import com.codeshare.airline.auth.authentication.provider.oidc.base.OidcStateManager;
import com.codeshare.airline.auth.authentication.service.core.*;
import com.codeshare.airline.auth.authentication.service.source.TenantIdentityProviderSelector;
import com.codeshare.airline.auth.authentication.state.OidcStatePayload;
import com.codeshare.airline.auth.model.entities.OidcStateEntity;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class LoginController {


    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    private final AuthenticationService authenticationService;
    private final TenantContextResolver tenantContextResolver;
    private final TenantIdentityProviderSelector tenantIdentityProviderSelector;
    private final TokenService tokenService;
    private final AuthenticationProviderResolver authenticationProviderResolver;
    private final OidcStateManager oidcStateManager;


    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request,
            @RequestHeader("tenant_code") String tenantCode
    ) {

        if (!StringUtils.hasText(tenantCode)) {
            throw new AuthenticationFailedException("Tenant code missing");
        }

        TenantContext tenant = tenantContextResolver.resolveTenant(tenantCode);

        IdentityProviderConfig idpConfig =
                tenantIdentityProviderSelector.select(
                        tenant,
                        request.getRequestedAuthSource()
                );

        AuthenticationResult authResult =
                authenticationService.authenticate(
                        request.withTenantAndProvider(tenant, idpConfig)
                );

        TokenPair tokens = tokenService.issueTokens(authResult);

        return LoginResponse.builder()
                .username(authResult.getUsername())
                .tenantCode(authResult.getTenantCode())
                .roles(authResult.getRoles())
                .permissions(authResult.getPermissions())
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .expiresIn(tokenService.getAccessTokenTtl())
                .build();
    }



    @PostMapping("/refresh")
    public RefreshTokenResponse refresh(
            @RequestHeader("X-Refresh-Token") String refreshToken
    ) {

        if (!StringUtils.hasText(refreshToken)) {
            throw new AuthenticationFailedException("Refresh token missing");
        }

        TokenPair tokens = tokenService.refreshTokens(refreshToken);

        return RefreshTokenResponse.builder()
                .accessToken(tokens.getAccessToken())
                .expiresIn(tokenService.getAccessTokenTtl())
                .build();
    }



    @PostMapping("/logout")
    public void logout(
            @RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken
    ) {
        if (StringUtils.hasText(refreshToken)) {
            tokenService.revokeSession(refreshToken);
        }
    }


    @GetMapping("/oidc/authorize")
    public void authorize(
            @RequestParam("tenant") String tenantCode,
            @RequestParam("code_challenge") String codeChallenge,
            HttpServletResponse response
    ) throws IOException {

        TenantContext tenant = tenantContextResolver.resolveTenant(tenantCode);

        IdentityProviderConfig idpConfig =
                tenantIdentityProviderSelector.select(tenant, null);

        AuthenticationProvider provider =
                authenticationProviderResolver.resolve(idpConfig.getAuthSource());

        if (!(provider instanceof AuthorizationRedirectCapable redirectCapable)) {
            throw new UnsupportedAuthenticationFlowException("Provider is not OIDC");
        }

        String state = tokenService.createOidcState(tenantCode,codeChallenge);


        OidcStateManager.OidcAuthorizationRequest req =
                oidcStateManager.createAuthorizationRequest(tenantCode,codeChallenge );



        String redirectUrl = redirectCapable.buildAuthorizeUrl(
                tenant,
                idpConfig,
                req.state(),
                codeChallenge,
                req.nonce()
        );

        response.sendRedirect(redirectUrl);
    }



    @GetMapping("/oidc/callback")
    public void callback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            @RequestParam("tenant") String tenantCode,
            @RequestHeader("X-Code-Verifier") String codeVerifier,
            HttpServletResponse response
    ) throws IOException {

        // 1️⃣ Verify & consume OIDC state (CSRF protection)
        OidcStatePayload statePayload =
                oidcStateManager.verifyAndConsumeState(state);

        // 2️⃣ Verify PKCE (authorization code binding)
        tokenService.verifyPkce(codeVerifier,statePayload.getCodeChallenge());

        // 3️⃣ Resolve tenant & provider
        TenantContext tenant = tenantContextResolver.resolveTenant(tenantCode);

        IdentityProviderConfig idpConfig = tenantIdentityProviderSelector.select(tenant, null);

        AuthenticationProvider provider =
                authenticationProviderResolver.resolve(idpConfig.getAuthSource());

        // 4️⃣ Exchange authorization code WITH PKCE
        AuthenticationResult authResult =
                provider.authenticate(
                        LoginRequest.builder()
                                .authorizationCode(code)
                                .codeVerifier(codeVerifier) // 🔐 REQUIRED
                                .tenant(tenant)
                                .identityProviderConfig(idpConfig)
                                .build()
                );

        // 5️⃣ Validate & consume nonce (ID token replay protection)
        oidcStateManager.consumeNonce(statePayload.getNonce());

        // 6️⃣ Issue INTERNAL tokens
        TokenPair tokens = tokenService.issueTokens(authResult);

        // 7️⃣ Create one-time exchange code
        String exchangeCode = tokenService.createExchangeCode(tokens, authResult);

        // 8️⃣ Redirect frontend (NO TOKENS IN URL)
        response.sendRedirect(frontendBaseUrl + "/oidc/callback?code=" + exchangeCode);
    }


    @PostMapping("/oidc/token")
    public LoginResponse exchange(@RequestBody OidcTokenExchangeRequest request) {

        TokenPair tokens = tokenService.exchangeTokens(request.getCode());

        return LoginResponse.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .expiresIn(tokenService.getAccessTokenTtl())
                .build();
    }


}
