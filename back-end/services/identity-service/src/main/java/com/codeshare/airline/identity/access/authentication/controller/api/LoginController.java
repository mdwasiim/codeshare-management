package com.codeshare.airline.identity.access.authentication.controller.api;

import com.codeshare.airline.core.constants.CSMConstants;
import com.codeshare.airline.core.response.CSMServiceResponse;
import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import com.codeshare.airline.identity.access.assignments.repository.UserGroupRepository;
import com.codeshare.airline.identity.access.assignments.service.RolePermissionAssignmentService;
import com.codeshare.airline.identity.access.authentication.core.api.request.LoginRequest;
import com.codeshare.airline.identity.access.authentication.core.api.request.OidcTokenExchangeRequest;
import com.codeshare.airline.identity.access.authentication.core.api.response.AuthSessionResponse;
import com.codeshare.airline.identity.access.authentication.core.api.response.LoginResponse;
import com.codeshare.airline.identity.access.authentication.core.api.response.RefreshTokenResponse;
import com.codeshare.airline.identity.access.authentication.core.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.access.authentication.core.domain.OidcConfig;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.identity.access.authentication.core.domain.TokenPair;
import com.codeshare.airline.identity.access.authentication.core.exception.AuthenticationFailedException;
import com.codeshare.airline.identity.access.authentication.core.exception.UnsupportedAuthenticationFlowException;
import com.codeshare.airline.identity.access.authentication.core.provider.AuthenticationProvider;
import com.codeshare.airline.identity.access.authentication.core.provider.AuthorizationRedirectCapable;
import com.codeshare.airline.identity.access.authentication.core.provider.oidc.base.OidcStateManager;
import com.codeshare.airline.identity.access.authentication.core.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.identity.access.authentication.core.service.core.*;
import com.codeshare.airline.identity.access.authentication.core.service.source.TenantIdentityProviderSelector;
import com.codeshare.airline.identity.access.authentication.core.state.OidcStatePayload;
import com.codeshare.airline.identity.access.identity.service.AuthUserService;
import com.codeshare.airline.web.response.CSMResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@CSMResponse
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationService authenticationService;
    private final TenantContextResolver tenantContextResolver;
    private final TenantIdentityProviderSelector tenantIdentityProviderSelector;
    private final TokenService tokenService;
    private final AuthenticationProviderResolver authenticationProviderResolver;
    private final OidcStateManager oidcStateManager;
    private final AuthUserService authUserService;
    private final RolePermissionAssignmentService rolePermissionAssignmentService;
    private final UserGroupRepository userGroupRepository;

    // -------------------------------------------------
    // LOGIN
    // -------------------------------------------------
    @PostMapping("/login")
    public LoginResponse login(@RequestHeader("X-Tenant-Id") String tenantCode,@RequestBody LoginRequest request) {

        TenantContext tenant = tenantContextResolver.resolveTenant(tenantCode);

        log.info("Login request received | tenant={}", tenant.getTenantCode());

        IdentityProviderConfig idpConfig = tenantIdentityProviderSelector.select(tenant, request.getAuthSource());

        log.debug("Selected IdentityProvider | ingestion={} providerId={} authSource={}",tenant.getTenantCode(),idpConfig.getProviderId(),idpConfig.getAuthSource());

        AuthenticationResult authResult = authenticationService.authenticate(request.withTenantAndProvider(tenant, idpConfig));

        log.info("Authentication successful | user={} ingestion={}",authResult.getUsername(),authResult.getTenantCode());

        TokenPair tokens = tokenService.issueTokens(authResult);

        log.debug("Tokens issued successfully | user={} ingestion={}",authResult.getUsername(),authResult.getTenantCode());

        return LoginResponse.builder()
                .userId(authResult.getUserId())
                .username(authResult.getUsername())
                .email(authResult.getEmail())
                .tenantCode(authResult.getTenantCode())
                .tenantId(authResult.getTenantId().toString())
                .groups(authResult.getUserGroups())
                .roles(authResult.getRoles())
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .expiresIn(tokenService.getAccessTokenTtl())
                .build();

    }

    @GetMapping("/session")
    public AuthSessionResponse session(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null || !StringUtils.hasText(jwt.getSubject())) {
            throw new AuthenticationFailedException("Invalid session");
        }

        UserDetailsAdapter user = authUserService.getAuthUserByUsername(jwt.getSubject());
        if (user == null || !user.isEnabled()) {
            throw new AuthenticationFailedException("User session is no longer active");
        }

        Set<String> groups = userGroupRepository.findByUserIdWithGroup(user.getUserId()).stream()
                .map(UserGroup::getGroup)
                .map(group -> group.getCode())
                .collect(Collectors.toSet());

        return AuthSessionResponse.builder()
                .userId(user.getUserId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .tenantId(user.getTenantId().toString())
                .tenantCode(user.getTenantCode())
                .groups(groups)
                .roles(rolePermissionAssignmentService.resolveRoleCodes(user.getUserId()))
                .permissions(rolePermissionAssignmentService.resolvePermissionCodes(user.getUserId()))
                .build();
    }

    // -------------------------------------------------
    // REFRESH TOKEN
    // -------------------------------------------------
    @PostMapping("/refresh")
    public RefreshTokenResponse refresh( @RequestHeader("X-Tenant-Id") String tenantCode, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        log.debug("Refresh token request received");
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthenticationFailedException("Invalid Authorization header");
        }
        String refreshToken = authorizationHeader.substring(7);
        if (!StringUtils.hasText(refreshToken)) {
            log.warn("Refresh token missing in request");
            throw new AuthenticationFailedException("Refresh token missing");
        }
        //  Explicit ingestion validation
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
    public CSMServiceResponse<String> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader
    ) {
        String refreshToken = extractToken(authorizationHeader);

        Jwt jwt = tokenService.validateRefreshToken(refreshToken);

        log.info("Logout | user={} tenant={} jti={}",
                jwt.getSubject(),
                jwt.getClaimAsString("tenant_code"),
                jwt.getId()
        );

        tokenService.revokeSession(refreshToken);

        return CSMServiceResponse.success(CSMConstants.LOGOUT_SUCCESS);
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

        if (!tenantCode.equals(statePayload.getTenantCode())) {
            log.warn("OIDC callback rejected due to tenant mismatch | requestTenant={} stateTenant={}",
                    tenantCode,
                    statePayload.getTenantCode()
            );
            throw new AuthenticationFailedException("Tenant mismatch in OIDC callback");
        }

        tokenService.verifyPkce(codeVerifier, statePayload.getCodeChallenge());

        TenantContext tenant = tenantContextResolver.resolveTenant(tenantCode);
        IdentityProviderConfig idpConfig =
                tenantIdentityProviderSelector.select(tenant, null);

        if (!idpConfig.getProviderId().equals(statePayload.getProviderId())) {
            log.warn("OIDC callback rejected due to provider mismatch | selectedProvider={} stateProvider={}",
                    idpConfig.getProviderId(),
                    statePayload.getProviderId()
            );
            throw new AuthenticationFailedException("Identity provider mismatch in OIDC callback");
        }

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

    private String extractToken(String header) {
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            throw new AuthenticationFailedException("Invalid Authorization header");
        }
        return header.substring(7);
    }
}
