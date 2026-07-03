package com.codeshare.airline.identity.access.authentication.controller.api;

import com.codeshare.airline.identity.access.authentication.core.security.key.SigningKeyProvider;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
public class JwksController {

    private final SigningKeyProvider keyProvider;


    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        return keyProvider.getPublicJwkSet().toJSONObject();
    }
}
