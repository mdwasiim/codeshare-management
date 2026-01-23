package com.codeshare.airline.core.dto.tenant;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PermissionDTO extends CSMAuditableDTO {

    private UUID id;

    private String name;            // Human-readable name (e.g., "User Create")
    private String code;            // domain:action (auto-generated)

    private String description;     // Detailed meaning

    private String domain;          // NEW — example: "user"
    private String action;          // NEW — example: "create"

    private UUID tenantId;          // Tenant ownership
}
