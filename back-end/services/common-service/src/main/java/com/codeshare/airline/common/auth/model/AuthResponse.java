package com.codeshare.airline.common.auth.model;

import com.codeshare.airline.common.utils.mapper.audit.AuditBaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse extends AuditBaseDto {
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
    private Set<String> roles;
    private Set<String> permissions;
}
