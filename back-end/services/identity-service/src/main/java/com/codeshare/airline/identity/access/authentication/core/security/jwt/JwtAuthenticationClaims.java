package com.codeshare.airline.identity.access.authentication.core.security.jwt;


import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import com.codeshare.airline.platform.core.enums.auth.TokenType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
public class JwtAuthenticationClaims {

    private final Long userId;
    private final String username;
    private final String subject; // username / userId
    private final String tenantCode;
    private final Long tenantId;

    private final AuthSource authSource;
    private final TokenType tokenType;

    private final Set<String> roles;

    private final String jti;
    private final LocalDateTime issuedAt;
    private final LocalDateTime expiresAt;

}
