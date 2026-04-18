package com.codeshare.airline.identity.authentication.state;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OidcStatePayload {

    // 🔐 CSRF / replay protection
    private String stateId;

    // 🏢 Multi-ingestion binding
    private String tenantCode;

    // 🔐 OIDC replay protection (ID Token)
    private String nonce;

    // 🔐 PKCE binding
    private String codeChallenge;

    // 🔒 Context binding (IMPORTANT)
    private String providerId;     // issuer / IdP key
    private String redirectUri;    // callback URL

    // ⏱ Lifetime control
    private Instant issuedAt;
    private Instant expiresAt;

    public boolean isExpired() {
        return expiresAt == null || expiresAt.isBefore(Instant.now());
    }
}
