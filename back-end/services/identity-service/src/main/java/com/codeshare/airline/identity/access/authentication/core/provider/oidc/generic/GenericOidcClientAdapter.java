package com.codeshare.airline.identity.access.authentication.core.provider.oidc.generic;

import com.codeshare.airline.identity.access.authentication.core.api.response.TokenPairResponse;
import com.codeshare.airline.identity.access.authentication.core.domain.IdentityProviderConfig;
import com.codeshare.airline.identity.access.authentication.core.domain.OidcAuthenticatedUser;
import com.codeshare.airline.identity.access.authentication.core.exception.AuthenticationFailedException;
import com.codeshare.airline.identity.access.authentication.core.provider.oidc.azure.AzureAudienceClaimValidator;
import com.codeshare.airline.identity.access.authentication.core.provider.oidc.base.OidcClientAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GenericOidcClientAdapter implements OidcClientAdapter {

    private final RestTemplate restTemplate;

    @Override
    public OidcAuthenticatedUser exchangeCodeForUser(String authorizationCode, String codeVerifier, String redirectUri, IdentityProviderConfig config) {
        TokenPairResponse token = exchangeCodeForToken(authorizationCode, codeVerifier, redirectUri, config);
        Jwt idToken = decodeAndValidate(token.getIdToken(), config);
        return extractOidcUser(idToken);
    }

    private TokenPairResponse exchangeCodeForToken(
            String authorizationCode,
            String codeVerifier,
            String redirectUri,
            IdentityProviderConfig config
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", config.getOidcConfig().getClientId());
        body.add("client_secret", config.getOidcConfig().getClientSecretRef());
        body.add("code", authorizationCode);
        body.add("code_verifier", codeVerifier);
        body.add("redirect_uri", redirectUri);

        ResponseEntity<TokenPairResponse> response = restTemplate.postForEntity(
                config.getOidcConfig().getTokenUri(),
                new HttpEntity<>(body, headers),
                TokenPairResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new AuthenticationFailedException("OIDC token exchange failed");
        }

        return response.getBody();
    }

    private Jwt decodeAndValidate(String idToken, IdentityProviderConfig config) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(config.getOidcConfig().getJwkSetUri()).build();
        OAuth2TokenValidator<Jwt> issuer = JwtValidators.createDefaultWithIssuer(config.getOidcConfig().getIssuerUri());
        OAuth2TokenValidator<Jwt> audience = new AzureAudienceClaimValidator(config.getOidcConfig().getClientId());
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(issuer, audience));
        return decoder.decode(idToken);
    }

    private OidcAuthenticatedUser extractOidcUser(Jwt jwt) {
        String subject = jwt.getSubject();
        if (subject == null || subject.isBlank()) {
            throw new AuthenticationFailedException("OIDC ID token missing sub claim");
        }

        String username = firstNonBlank(
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("upn"),
                jwt.getClaimAsString("email"),
                subject
        );

        return OidcAuthenticatedUser.builder()
                .subject(subject)
                .externalId(subject)
                .username(username)
                .email(jwt.getClaimAsString("email"))
                .issuer(jwt.getIssuer() != null ? jwt.getIssuer().toString() : null)
                .build();
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
