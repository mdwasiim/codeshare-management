package com.codeshare.airline.auth.controller.api;

import com.codeshare.airline.auth.authentication.api.request.LoginRequest;
import com.codeshare.airline.auth.authentication.api.request.OidcTokenExchangeRequest;
import com.codeshare.airline.auth.authentication.api.response.LoginResponse;
import com.codeshare.airline.auth.authentication.api.response.RefreshTokenResponse;
import com.codeshare.airline.auth.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.OidcConfig;
import com.codeshare.airline.auth.authentication.domain.TenantContext;
import com.codeshare.airline.auth.authentication.domain.TokenPair;
import com.codeshare.airline.auth.authentication.exception.AuthenticationFailedException;
import com.codeshare.airline.auth.authentication.exception.UnsupportedAuthenticationFlowException;
import com.codeshare.airline.auth.authentication.provider.AuthenticationProvider;
import com.codeshare.airline.auth.authentication.provider.AuthorizationRedirectCapable;
import com.codeshare.airline.auth.authentication.provider.oidc.base.OidcStateManager;
import com.codeshare.airline.auth.authentication.service.core.*;
import com.codeshare.airline.auth.authentication.service.source.TenantIdentityProviderSelector;
import com.codeshare.airline.auth.authentication.state.OidcStatePayload;
import com.codeshare.airline.auth.common.CSMResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@CSMResponse
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationService authenticationService;
    private final TenantContextResolver tenantContextResolver;
    private final TenantIdentityProviderSelector tenantIdentityProviderSelector;
    private final TokenService tokenService;
    private final AuthenticationProviderResolver authenticationProviderResolver;
    private final OidcStateManager oidcStateManager;

    // -------------------------------------------------
    // LOGIN
    // -------------------------------------------------
    @PostMapping("/login")
    public LoginResponse login(@RequestHeader("tenant-code") String tenantCode,@RequestBody LoginRequest request) {

        TenantContext tenant = tenantContextResolver.resolveTenant(tenantCode);

        log.info("Login request received | ingestion={} authSource={}",tenant.getTenantCode(),request.getRequestedAuthSource());

        IdentityProviderConfig idpConfig = tenantIdentityProviderSelector.select(tenant, request.getRequestedAuthSource());

        log.debug("Selected IdentityProvider | ingestion={} providerId={} authSource={}",tenant.getTenantCode(),idpConfig.getProviderId(),idpConfig.getAuthSource());

        AuthenticationResult authResult = authenticationService.authenticate(request.withTenantAndProvider(tenant, idpConfig));

        log.info("Authentication successful | user={} ingestion={}",authResult.getUsername(),authResult.getTenantCode());

        TokenPair tokens = tokenService.issueTokens(authResult);

        log.debug("Tokens issued successfully | user={} ingestion={}",authResult.getUsername(),authResult.getTenantCode());

        LoginResponse loginResponse = LoginResponse.builder()
                .username(authResult.getUsername())
                .tenantCode(authResult.getTenantCode())
                .roles(authResult.getRoles())
                .permissions(authResult.getPermissions())
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .expiresIn(tokenService.getAccessTokenTtl())
                .build();
        return loginResponse;
    }

    // -------------------------------------------------
    // REFRESH TOKEN
    // -------------------------------------------------
    @PostMapping("/refresh")
    public RefreshTokenResponse refresh( @RequestHeader("tenant-code") String tenantCode,@RequestHeader(value = "refresh-token", required = false) String refreshToken) {

        log.debug("Refresh token request received");

        if (!StringUtils.hasText(refreshToken)) {
            log.warn("Refresh token missing in request");
            throw new AuthenticationFailedException("Refresh token missing");
        }
        // ✅ Explicit ingestion validation
        tenantContextResolver.validateTenant(tenantCode);

        TokenPair tokens = tokenService.refreshTokens(refreshToken);

        log.info("Access token refreshed successfully");

        return RefreshTokenResponse.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .expiresIn(tokenService.getAccessTokenTtl())
                .build();
    }

    // -------------------------------------------------
    // LOGOUT
    // -------------------------------------------------
    @PostMapping("/logout")
    public void logout(
            @RequestHeader(value = "X-Refresh-Token", required = false) String refreshToken
    ) {
        if (StringUtils.hasText(refreshToken)) {
            log.info("Logout request received – revoking session");
            tokenService.revokeSession(refreshToken);
        } else {
            log.debug("Logout request without refresh token");
        }
    }

    // -------------------------------------------------
    // OIDC AUTHORIZE
    // -------------------------------------------------
    @GetMapping("/oidc/authorize")
    public void authorize(
            @RequestParam("ingestion") String tenantCode,
            @RequestParam("code_challenge") String codeChallenge,
            HttpServletResponse response
    ) throws IOException {

        log.info("OIDC authorize request | ingestion={}", tenantCode);

        if (!StringUtils.hasText(codeChallenge)) {
            log.warn("OIDC authorize failed – missing code_challenge | ingestion={}", tenantCode);
            throw new IllegalArgumentException("code_challenge is required");
        }

        TenantContext tenant = tenantContextResolver.resolveTenant(tenantCode);
        IdentityProviderConfig idpConfig =
                tenantIdentityProviderSelector.select(tenant, null);

        AuthenticationProvider provider =
                authenticationProviderResolver.resolve(idpConfig.getAuthSource());

        if (!(provider instanceof AuthorizationRedirectCapable redirectCapable)) {
            log.error("OIDC authorize failed – provider not redirect-capable | provider={}",
                    idpConfig.getAuthSource()
            );
            throw new UnsupportedAuthenticationFlowException("Provider is not OIDC-capable");
        }

        OidcConfig oidc = idpConfig.getOidcConfig();

        OidcStateManager.OidcAuthorizationRequest req =
                oidcStateManager.createAuthorizationRequest(
                        tenantCode,
                        idpConfig.getProviderId(),
                        oidc.getRedirectUri(),
                        codeChallenge
                );

        String redirectUrl = redirectCapable.buildAuthorizeUrl(
                tenant,
                idpConfig,
                req.state(),
                codeChallenge,
                req.nonce()
        );

        log.info("Redirecting to OIDC provider | ingestion={} provider={}",
                tenantCode,
                idpConfig.getProviderId()
        );

        response.sendRedirect(redirectUrl);
    }

    // -------------------------------------------------
    // OIDC CALLBACK
    // -------------------------------------------------
    @GetMapping("/oidc/callback")
    public void callback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            @RequestParam("ingestion") String tenantCode,
            @RequestHeader("X-Code-Verifier") String codeVerifier,
            HttpServletResponse response
    ) throws IOException {

        log.info("OIDC callback received | ingestion={}", tenantCode);

        OidcStatePayload statePayload =
                oidcStateManager.verifyAndConsumeState(state);

        tokenService.verifyPkce(codeVerifier, statePayload.getCodeChallenge());

        TenantContext tenant = tenantContextResolver.resolveTenant(tenantCode);
        IdentityProviderConfig idpConfig =
                tenantIdentityProviderSelector.select(tenant, null);

        AuthenticationProvider provider =
                authenticationProviderResolver.resolve(idpConfig.getAuthSource());

        AuthenticationResult authResult =
                provider.authenticate(
                        LoginRequest.builder()
                                .authorizationCode(code)
                                .codeVerifier(codeVerifier)
                                .tenant(tenant)
                                .identityProviderConfig(idpConfig)
                                .build()
                );

        oidcStateManager.consumeNonce(statePayload.getNonce());

        TokenPair tokens = tokenService.issueTokens(authResult);
        String exchangeCode = tokenService.createExchangeCode(tokens, authResult);

        log.info("OIDC authentication successful | user={} ingestion={}",
                authResult.getUsername(),
                tenantCode
        );

        response.sendRedirect(
                idpConfig.getOidcConfig().getRedirectUri()
                        + "/oidc/callback?code=" + exchangeCode
        );
    }

    // -------------------------------------------------
    // OIDC TOKEN EXCHANGE
    // -------------------------------------------------
    @PostMapping("/oidc/token")
    public LoginResponse exchange(@RequestBody OidcTokenExchangeRequest request) {

        log.debug("OIDC token exchange request received");

        TokenPair tokens = tokenService.exchangeTokens(request.getCode());

        log.info("OIDC token exchange successful");

        return LoginResponse.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .expiresIn(tokenService.getAccessTokenTtl())
                .build();
    }
}
