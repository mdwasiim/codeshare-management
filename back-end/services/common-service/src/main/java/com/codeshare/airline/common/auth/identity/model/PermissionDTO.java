package com.codeshare.airline.common.auth.identity.model;

import com.codeshare.airline.common.services.audit.AuditBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO extends AuditBaseDTO {

    private UUID id;

    private String name;            // Human-readable name (e.g., "User Create")
    private String code;            // domain:action (auto-generated)

    private String description;     // Detailed meaning

    private String domain;          // NEW — example: "user"
    private String action;          // NEW — example: "create"

    private UUID tenantId;          // Tenant ownership
}
