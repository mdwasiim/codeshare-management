package com.codeshare.airline.platform.core.dto.tenant;

import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityProviderConfigDTO {

    private AuthSource authSource;
    private boolean enabled;
    private int priority;
    private String providerId;
    private OidcConfigDTO oidcConfig;
}
