package com.codeshare.airline.auth.authentication.state;

import com.codeshare.airline.auth.authentication.exception.AuthenticationFailedException;
import com.codeshare.airline.auth.authentication.security.crypto.HmacSigner;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class OidcStateSigner {

    private final ObjectMapper objectMapper;

    @Value("${security.oidc.state.secret}")
    private String secret;

    @Value("${security.oidc.state.ttl-seconds:300}")
    private long ttlSeconds;

    public String encode(OidcStatePayload payload) {

        try {
            payload.setIssuedAt(Instant.now()); // ensure timestamp

            String json = objectMapper.writeValueAsString(payload);
            String signature = HmacSigner.sign(json, secret);

            return Base64.getUrlEncoder()
                    .encodeToString((json + "." + signature)
                            .getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            throw new IllegalStateException("Failed to encode OIDC state", e);
        }
    }

    public OidcStatePayload verifyAndDecode(String state) {

        try {
            String decoded =
                    new String(Base64.getUrlDecoder().decode(state),
                            StandardCharsets.UTF_8);

            String[] parts = decoded.split("\\.", 2);
            if (parts.length != 2) {
                throw new AuthenticationFailedException("Invalid state format");
            }

            String json = parts[0];
            String signature = parts[1];

            if (!HmacSigner.verify(json, signature, secret)) {
                throw new AuthenticationFailedException("State signature invalid");
            }

            OidcStatePayload payload =
                    objectMapper.readValue(json, OidcStatePayload.class);

            validateExpiration(payload);

            return payload;

        } catch (AuthenticationFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationFailedException("Invalid state"+ e);
        }
    }

    private void validateExpiration(OidcStatePayload payload) {

        if (payload.getIssuedAt() == null) {
            throw new AuthenticationFailedException("State missing issuedAt");
        }

        Instant expiry =
                payload.getIssuedAt().plusSeconds(ttlSeconds);

        if (Instant.now().isAfter(expiry)) {
            throw new AuthenticationFailedException("State expired");
        }
    }
}
