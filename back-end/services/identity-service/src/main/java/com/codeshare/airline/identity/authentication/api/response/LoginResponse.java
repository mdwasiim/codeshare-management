package com.codeshare.airline.identity.authentication.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class LoginResponse {

    @JsonProperty("user_id")
    private final String userId;

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("tenant_code")
    private String tenantCode;

    @JsonProperty("groups")
    private Set<String> groups;

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
