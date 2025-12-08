package com.codeshare.airline.common.auth.identity.model;

import com.codeshare.airline.common.services.audit.AuditBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse extends AuditBaseDTO {
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
    private Set<String> roles;
    private Set<String> permissions;
}
