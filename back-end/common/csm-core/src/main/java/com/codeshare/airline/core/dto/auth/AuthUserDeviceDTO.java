package com.codeshare.airline.core.dto.auth;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
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
public class AuthUserDeviceDTO extends CSMAuditableDTO {
    private String deviceId;

    private String userAgent;

    private String ipAddress;

    private UUID tenantId;

    private AuthUserDTO user;

    private LocalDateTime lastSeen;
}
