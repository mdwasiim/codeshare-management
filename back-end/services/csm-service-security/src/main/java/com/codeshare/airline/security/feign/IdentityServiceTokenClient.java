package com.codeshare.airline.security.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "identity-service-token",
        url = "${services.identity.url:http://localhost:8081}"
)
public interface IdentityServiceTokenClient {

    @PostMapping("/auth/service-token")
    InternalServiceTokenResponse issueServiceToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader);

    @Getter
    @Setter
    class InternalServiceTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private long expiresIn;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("scope")
        private String scope;
    }
}
