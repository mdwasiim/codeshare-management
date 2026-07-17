package com.codeshare.airline.platform.core.dto.auth;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserDeviceDTO extends CSMAuditableDTO {
    private String deviceId;

    private String userAgent;

    private String ipAddress;

    private Long tenantId;

    private AuthUserDTO user;

    private LocalDateTime lastSeen;
}
