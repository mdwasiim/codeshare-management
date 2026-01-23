package com.codeshare.airline.auth.authentication.state;

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

    private String stateId;
    private String tenantCode;

    // 🔐 OIDC replay protection
    private String nonce;

    // 🔐 PKCE binding
    private String codeChallenge;

    private Instant issuedAt;
    private Instant expiresAt;

    public boolean isExpired() {
        return expiresAt.isBefore(Instant.now());
    }
}
