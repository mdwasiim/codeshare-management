package com.codeshare.airline.auth.authentication.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenPairResponse {

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("refresh_token")
    private final String refreshToken;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("scope")
    private String scope;



}
