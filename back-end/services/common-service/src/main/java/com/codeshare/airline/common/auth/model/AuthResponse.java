package com.codeshare.airline.common.auth.model;


import com.codeshare.airline.common.utils.mapper.audit.AuditBaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AuthResponse extends AuditBaseDto {
    private String accessToken;
    private String refreshToken;
    private UserDTO userDTO;
    private Set<String> roles;
    private Set<String> permissions;

}
