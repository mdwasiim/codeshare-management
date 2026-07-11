package com.codeshare.airline.identity.access.authentication.controller.api;

import com.codeshare.airline.platform.core.constants.CSMConstants;
import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import com.codeshare.airline.platform.core.response.CSMServiceResponse;
import com.codeshare.airline.identity.access.assignments.entities.UserGroup;
import com.codeshare.airline.identity.access.assignments.repository.UserGroupRepository;
import com.codeshare.airline.identity.access.assignments.service.RolePermissionAssignmentService;
import com.codeshare.airline.identity.access.authentication.core.api.request.LoginRequest;
import com.codeshare.airline.identity.access.authentication.core.api.request.OidcTokenExchangeRequest;
import com.codeshare.airline.identity.access.authentication.core.api.response.AuthSessionResponse;
import com.codeshare.airline.identity.access.authentication.core.api.response.LoginResponse;
import com.codeshare.airline.identity.access.authentication.core.api.response.RefreshTokenResponse;
import com.codeshare.airline.identity.access.authentication.core.api.response.TokenPairResponse;
import com.codeshare.airline.identity.access.authentication.core.config.SecurityProperties;
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
import com.codeshare.airline.identity.access.authentication.core.service.core.AuthenticationProviderResolver;
import com.codeshare.airline.identity.access.authentication.core.service.core.AuthenticationResult;
import com.codeshare.airline.identity.access.authentication.core.service.core.AuthenticationService;
import com.codeshare.airline.identity.access.authentication.core.service.core.TenantContextResolver;
import com.codeshare.airline.identity.access.authentication.core.service.core.TokenService;
import com.codeshare.airline.identity.access.authentication.core.service.source.TenantIdentityProviderSelector;
import com.codeshare.airline.identity.access.authentication.core.state.OidcStatePayload;
import com.codeshare.airline.identity.access.identity.service.AuthUserService;
import com.codeshare.airline.platform.web.response.CSMResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    private final SecurityProperties securityProperties;

    @PostMapping("/login")
    public LoginResponse login(@RequestHeader("X-Tenant-Id") String tenantCode, @RequestBody LoginRequest request) {
        TenantContext tenant = tenantContextResolver.resolveTenant(tenantCode);
        IdentityProviderConfig idpConfig = tenantIdentityProviderSelector.select(tenant, request.getAuthSource());

        AuthenticationResult authResult = authenticationService.authenticate(request.withTenantAndProvider(tenant, idpConfig));
        TokenPair tokens = tokenService.issueTokens(authResult);

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

    @PostMapping("/refresh")
    public RefreshTokenResponse refresh(
            @RequestHeader("X-Tenant-Id") String tenantCode,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthenticationFailedException("Invalid Authorization header");
        }

        String refreshToken = authorizationHeader.substring(7);
        tenantContextResolver.validateTenant(tenantCode);

        TokenPair tokens = tokenService.refreshTokens(refreshToken);

        return RefreshTokenResponse.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .expiresIn(tokenService.getAccessTokenTtl())
                .build();
    }

    @PostMapping("/service-token")
    public TokenPairResponse issueServiceToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        ServiceCredentials credentials = extractBasicCredentials(authorizationHeader);

        if (!securityProperties.getS2s().getClientId().equals(credentials.clientId())
                || !securityProperties.getS2s().getClientSecret().equals(credentials.clientSecret())) {
            throw new AuthenticationFailedException("Invalid service client credentials");
        }

        String accessToken = tokenService.issueServiceAccessToken(credentials.clientId());

        return TokenPairResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(tokenService.getAccessTokenTtl())
                .scope("internal")
                .build();
    }

    @PostMapping("/logout")
    public CSMServiceResponse<String> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader
    ) {
        String refreshToken = extractToken(authorizationHeader);
        Jwt jwt = tokenService.validateRefreshToken(refreshToken);

        log.info("Logout | user={} tenant={} jti={}", jwt.getSubject(), jwt.getClaimAsString("tenant_code"), jwt.getId());
        tokenService.revokeSession(refreshToken);

        return CSMServiceResponse.success(CSMConstants.LOGOUT_SUCCESS);
    }

    @GetMapping("/oidc/authorize")
    public void authorize(
            @RequestParam("tenantCode") String tenantCode,
            @RequestParam(value = "authSource", required = false) AuthSource authSource,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        TenantContext tenant = tenantContextResolver.resolveTenant(tenantCode);
        IdentityProviderConfig idpConfig = tenantIdentityProviderSelector.select(tenant, authSource);

        AuthenticationProvider provider = authenticationProviderResolver.resolve(idpConfig.getAuthSource());
        if (!(provider instanceof AuthorizationRedirectCapable redirectCapable)) {
            throw new UnsupportedAuthenticationFlowException("Provider is not OIDC-capable");
        }

        OidcConfig oidc = idpConfig.getOidcConfig();
        if (oidc == null || !StringUtils.hasText(oidc.getRedirectUri())) {
            throw new AuthenticationFailedException("OIDC redirect configuration missing");
        }

        String callbackUri = buildBackendCallbackUrl(request);

        OidcStateManager.OidcAuthorizationRequest authorizationRequest = oidcStateManager.createAuthorizationRequest(
                tenantCode,
                idpConfig.getProviderId(),
                oidc.getRedirectUri(),
                callbackUri
        );

        String redirectUrl = redirectCapable.buildAuthorizeUrl(
                tenant,
                idpConfig,
                callbackUri,
                authorizationRequest.state(),
                authorizationRequest.codeChallenge(),
                authorizationRequest.nonce()
        );

        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/oidc/callback")
    public void callback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletResponse response
    ) throws IOException {
        OidcStatePayload statePayload = oidcStateManager.verifyAndConsumeState(state);
        String tenantCode = statePayload.getTenantCode();

        tokenService.verifyPkce(statePayload.getCodeVerifier(), statePayload.getCodeChallenge());

        TenantContext tenant = tenantContextResolver.resolveTenant(tenantCode);
        IdentityProviderConfig idpConfig = tenantIdentityProviderSelector.select(tenant, null);

        if (!idpConfig.getProviderId().equals(statePayload.getProviderId())) {
            throw new AuthenticationFailedException("Identity provider mismatch in OIDC callback");
        }

        AuthenticationProvider provider = authenticationProviderResolver.resolve(idpConfig.getAuthSource());
        AuthenticationResult authResult = provider.authenticate(
                LoginRequest.builder()
                        .authorizationCode(code)
                        .codeVerifier(statePayload.getCodeVerifier())
                        .redirectUri(statePayload.getCallbackUri())
                        .tenant(tenant)
                        .identityProviderConfig(idpConfig)
                        .build()
        );

        oidcStateManager.consumeNonce(statePayload.getNonce());

        TokenPair tokens = tokenService.issueTokens(authResult);
        String exchangeCode = tokenService.createExchangeCode(tokens, authResult);

        response.sendRedirect(buildFrontendCallbackUrl(statePayload.getRedirectUri(), exchangeCode));
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

    private String extractToken(String header) {
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            throw new AuthenticationFailedException("Invalid Authorization header");
        }
        return header.substring(7);
    }

    private ServiceCredentials extractBasicCredentials(String header) {
        if (!StringUtils.hasText(header) || !header.startsWith("Basic ")) {
            throw new AuthenticationFailedException("Invalid Basic Authorization header");
        }

        try {
            String decoded = new String(java.util.Base64.getDecoder().decode(header.substring(6)), StandardCharsets.UTF_8);
            int separatorIndex = decoded.indexOf(':');
            if (separatorIndex < 0) {
                throw new AuthenticationFailedException("Invalid client credentials format");
            }
            String clientId = decoded.substring(0, separatorIndex);
            String clientSecret = decoded.substring(separatorIndex + 1);
            return new ServiceCredentials(clientId, clientSecret);
        } catch (IllegalArgumentException ex) {
            throw new AuthenticationFailedException("Invalid client credentials encoding");
        }
    }

    private record ServiceCredentials(String clientId, String clientSecret) {}

    private String buildFrontendCallbackUrl(String frontendRedirectUri, String exchangeCode) {
        return UriComponentsBuilder.fromUriString(frontendRedirectUri)
                .replaceQueryParam("code", exchangeCode)
                .build(true)
                .toUriString();
    }

    private String buildBackendCallbackUrl(HttpServletRequest request) {
        String currentUrl = request.getRequestURL().toString();
        String callbackUrl = currentUrl.replace("/authorize", "/callback");
        return UriComponentsBuilder.fromUriString(callbackUrl)
                .replaceQuery(null)
                .build(true)
                .toUriString();
    }
}
