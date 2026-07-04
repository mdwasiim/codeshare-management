package com.codeshare.airline.identity.access.authentication.core.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class AuthSessionResponse {

    @JsonProperty("user_id")
    private final String userId;

    @JsonProperty("username")
    private final String username;

    @JsonProperty("email")
    private final String email;

    @JsonProperty("tenant_id")
    private final String tenantId;

    @JsonProperty("tenant_code")
    private final String tenantCode;

    @JsonProperty("groups")
    private final Set<String> groups;

    @JsonProperty("roles")
    private final Set<String> roles;

    @JsonProperty("permissions")
    private final Set<String> permissions;
}
