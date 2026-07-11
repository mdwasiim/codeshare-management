package com.codeshare.airline.core.dto.tenant;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OidcConfigDTO {

    private String issuerUri;
    private String authorizationUri;
    private String tokenUri;
    private String jwkSetUri;
    private String clientId;
    private String clientSecretRef;
    private String redirectUri;
    private String scopes;
    private boolean enforceRedirectUri;
}
