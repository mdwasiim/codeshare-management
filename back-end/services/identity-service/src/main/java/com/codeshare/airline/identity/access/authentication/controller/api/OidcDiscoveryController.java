package com.codeshare.airline.identity.access.authentication.controller.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OidcDiscoveryController {

    @Value("${security.jwt.issuer}")
    private String issuer;

    @GetMapping("/.well-known/openid-configuration")
    public Map<String, Object> config() {
        return Map.of(
                "issuer", issuer,
                "jwks_uri", issuer + "/.well-known/jwks.json"
        );
    }
}