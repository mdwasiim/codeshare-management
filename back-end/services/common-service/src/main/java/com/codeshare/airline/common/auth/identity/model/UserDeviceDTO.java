package com.codeshare.airline.common.auth.identity.model;

import com.codeshare.airline.common.services.audit.AuditBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeviceDTO extends AuditBaseDTO {
    private String deviceId;

    private String userAgent;

    private String ipAddress;

    private UUID tenantId;

    private UserDTO user;

    private LocalDateTime lastSeen;
}
