package com.codeshare.airline.auth.authentication.domain;

import com.codeshare.airline.core.enums.AuthSource;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OidcState {

    private String state;
    private String nonce;
    private String tenantCode;
    private AuthSource provider;
    private LocalDateTime expiresAt;
}
