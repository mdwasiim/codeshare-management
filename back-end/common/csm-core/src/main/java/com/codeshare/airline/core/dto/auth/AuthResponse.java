package com.codeshare.airline.core.dto.auth;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuthResponse extends CSMAuditableDTO {
    private AuthUserDTO user;
    private Set<String> roles;
    private Set<String> permissions;
}
