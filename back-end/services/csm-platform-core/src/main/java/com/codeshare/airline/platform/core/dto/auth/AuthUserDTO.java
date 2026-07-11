package com.codeshare.airline.platform.core.dto.auth;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.platform.core.dto.tenant.TenantDTO;
import com.codeshare.airline.platform.core.enums.auth.AuthSource;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuthUserDTO extends CSMAuditableDTO {

    private UUID id;

    private String username;

    private AuthSource authSource;

    private RecordStatus recordStatus;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private boolean enabled;

    private boolean accountNonLocked;

    private boolean accountNonExpired;

    private boolean credentialsNonExpired;

    private LocalDateTime lastLogin;

    private String lastLoginProvider;

    private String externalId;

    private TenantDTO tenant;
}
