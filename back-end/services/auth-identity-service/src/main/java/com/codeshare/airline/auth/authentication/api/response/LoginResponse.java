package com.codeshare.airline.auth.authentication.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class LoginResponse {

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("tenant_code")
    private String tenantCode;

    @JsonProperty("roles")
    private Set<String> roles;

    @JsonProperty("permissions")
    private Set<String> permissions;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private final String refreshToken;

    @JsonProperty("expires_in")
    private long expiresIn;

}
