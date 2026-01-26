package com.codeshare.airline.auth.authentication.provider.oidc.azure;

import com.codeshare.airline.auth.authentication.api.response.TokenPairResponse;
import com.codeshare.airline.auth.authentication.domain.IdentityProviderConfig;
import com.codeshare.airline.auth.authentication.domain.OidcAuthenticatedUser;
import com.codeshare.airline.auth.authentication.exception.AuthenticationFailedException;
import com.codeshare.airline.auth.authentication.provider.oidc.base.OidcClientAdapter;
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
public class AzureOidcClientAdapter implements OidcClientAdapter {

    private final RestTemplate restTemplate;

    @Override
    public OidcAuthenticatedUser exchangeCodeForUser(String code, IdentityProviderConfig config ) {

        TokenPairResponse token = exchangeCodeForToken(code, config);
        Jwt idToken = decodeAndValidate(token.getIdToken(), config);

        return extractOidcUser(idToken);
    }

    /* ================================
       TOKEN EXCHANGE
       ================================ */
    private TokenPairResponse exchangeCodeForToken(
            String code,
            IdentityProviderConfig config
    ) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", config.getOidcConfig().getClientId());
        body.add("client_secret", config.getOidcConfig().getClientSecretRef());
        body.add("code", code);
        body.add("redirect_uri", config.getOidcConfig().getRedirectUri());
        body.add("scope", config.getOidcConfig().getScopes());

        HttpEntity<?> request = new HttpEntity<>(body, headers);

        ResponseEntity<TokenPairResponse> response =
                restTemplate.postForEntity(
                        config.getOidcConfig().getTokenUri(),
                        request,
                        TokenPairResponse.class
                );

        if (!response.getStatusCode().is2xxSuccessful()
                || response.getBody() == null) {
            throw new AuthenticationFailedException(
                    "Azure token exchange failed"
            );
        }

        return response.getBody();
    }

    /* ================================
       JWT DECODER
       ================================ */
    private Jwt decodeAndValidate(
            String idToken,
            IdentityProviderConfig config
    ) {

        NimbusJwtDecoder decoder =
                NimbusJwtDecoder
                        .withJwkSetUri(config.getOidcConfig().getJwkSetUri())
                        .build();

        OAuth2TokenValidator<Jwt> issuer =
                JwtValidators.createDefaultWithIssuer(
                        config.getOidcConfig().getIssuerUri()
                );

        OAuth2TokenValidator<Jwt> audience =
                new AzureAudienceClaimValidator(
                        config.getOidcConfig().getClientId()
                );

        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(issuer, audience)
        );

        return decoder.decode(idToken);
    }

    /* ================================
       CLAIM EXTRACTION (IDENTITY ONLY)
       ================================ */
    private OidcAuthenticatedUser extractOidcUser(Jwt jwt) {

        String externalId = jwt.getClaimAsString("oid");
        if (externalId == null) {
            throw new AuthenticationFailedException(
                    "Azure ID token missing oid claim"
            );
        }

        return OidcAuthenticatedUser.builder()
                .subject(jwt.getSubject())
                .externalId(externalId)
                .username(jwt.getClaimAsString("preferred_username"))
                .email(jwt.getClaimAsString("email"))
                .issuer(jwt.getIssuer().toString())
                .build();
    }
}
