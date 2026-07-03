package com.codeshare.airline.identity.access.authentication.core.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OidcTokenExchangeRequest {
    /**
     * One-time exchange code issued by backend after successful OIDC login.
     * Valid for a very short duration (e.g. 30–60 seconds).
     */
    @NotBlank
    private String code;
}
