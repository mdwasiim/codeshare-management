package com.codeshare.airline.auth.authentication.provider.oidc.base;

import com.codeshare.airline.auth.authentication.exception.AuthenticationFailedException;
import com.codeshare.airline.auth.authentication.state.OidcNonceStore;
import com.codeshare.airline.auth.authentication.state.OidcStatePayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OidcStateManager {


    private final ObjectMapper objectMapper;
    private final OidcNonceStore oidcNonceStore;

    @Value("${security.oidc.state.secret}")
    private String stateSecret;

    @Value("${security.oidc.state.ttl-seconds:300}")
    public static final long STATE_TTL_SECONDS = 300;

    @PostConstruct
    void validateSecret() {
        if (stateSecret == null || stateSecret.length() < 32) {
            throw new IllegalStateException(
                    "security.oidc.state.secret must be at least 32 characters"
            );
        }
    }

    /* =====================================================
      CREATE AUTHORIZATION REQUEST (STATE + NONCE + PKCE)
      ===================================================== */
    public  OidcAuthorizationRequest createAuthorizationRequest(
            String tenantCode,
            String codeChallenge
    ) {

        Instant now = Instant.now();

        OidcStatePayload payload = OidcStatePayload.builder()
                .stateId(UUID.randomUUID().toString())
                .tenantCode(tenantCode)
                .nonce(UUID.randomUUID().toString())
                .codeChallenge(codeChallenge)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(STATE_TTL_SECONDS))
                .build();

        try {
            String json = objectMapper.writeValueAsString(payload);
            String signature = sign(json);

            String state =
                    base64Url(json.getBytes(StandardCharsets.UTF_8))
                            + "."
                            + signature;

            return new OidcAuthorizationRequest(
                    state,
                    payload.getNonce()
            );

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to create OIDC authorization request", e
            );
        }
    }

    /* =====================================================
     VERIFY & CONSUME STATE (CSRF + PKCE BINDING)
     ===================================================== */
    public OidcStatePayload verifyAndConsumeState(String state) {

        if (state == null || !state.contains(".")) {
            throw new AuthenticationFailedException("Invalid OIDC state");
        }

        String[] parts = state.split("\\.", 2);
        if (parts.length != 2) {
            throw new AuthenticationFailedException("Malformed OIDC state");
        }

        String json = decodeBase64Url(parts[0]);
        String providedSignature = parts[1];

        if (!verify(json, providedSignature)) {
            throw new AuthenticationFailedException(
                    "OIDC state signature invalid"
            );
        }

        OidcStatePayload payload;
        try {
            payload = objectMapper.readValue(
                    json, OidcStatePayload.class
            );
        } catch (Exception e) {
            throw new AuthenticationFailedException(
                    "Invalid OIDC state payload"
            );
        }

        if (payload.isExpired()) {
            throw new AuthenticationFailedException(
                    "OIDC state expired"
            );
        }

        // 🔐 CSRF replay protection (stateId)
        if (oidcNonceStore.isUsed(payload.getStateId())) {
            throw new AuthenticationFailedException(
                    "OIDC state replay detected"
            );
        }

        oidcNonceStore.markUsed(payload.getStateId());

        return payload;
    }

    /* =====================================================
      NONCE CONSUMPTION (ID TOKEN REPLAY PROTECTION)
      ===================================================== */
    public void consumeNonce(String nonce) {

        if (oidcNonceStore.isUsed(nonce)) {
            throw new AuthenticationFailedException(
                    "OIDC nonce replay detected"
            );
        }

        oidcNonceStore.markUsed(nonce);
    }

    /* =====================================================
       SIGNING (HMAC-SHA256)
       ===================================================== */
    private String sign(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(
                new SecretKeySpec(
                        stateSecret.getBytes(StandardCharsets.UTF_8),
                        "HmacSHA256"
                )
        );
        return base64Url(
                mac.doFinal(data.getBytes(StandardCharsets.UTF_8))
        );
    }

    private boolean verify(String data, String signature) {
        try {
            byte[] expected =
                    Base64.getUrlDecoder().decode(sign(data));
            byte[] provided =
                    Base64.getUrlDecoder().decode(signature);
            return MessageDigest.isEqual(expected, provided);
        } catch (Exception e) {
            return false;
        }
    }

    /* =====================================================
       BASE64 URL
       ===================================================== */
    private String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String decodeBase64Url(String data) {
        return new String(
                Base64.getUrlDecoder().decode(data),
                StandardCharsets.UTF_8
        );
    }

    /* =====================================================
       DTO
       ===================================================== */
    public record OidcAuthorizationRequest(
            String state,
            String nonce
    ) {}
}