package com.codeshare.airline.auth.authentication.security.jwt;


import com.codeshare.airline.core.enums.AuthSource;
import com.codeshare.airline.core.enums.TokenType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
public class JwtAuthenticationClaims {

    private final UUID userId;
    private final String username;
    private final String subject; // username / userId
    private final String tenantCode;
    private final UUID tenantId;

    private final AuthSource authSource;
    private final TokenType tokenType;

    private final Set<String> roles;

    private final String jti;
    private final LocalDateTime issuedAt;
    private final LocalDateTime expiresAt;

}
