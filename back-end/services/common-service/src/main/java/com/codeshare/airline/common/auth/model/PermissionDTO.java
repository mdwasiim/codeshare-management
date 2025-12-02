package com.codeshare.airline.common.auth.model;

import com.codeshare.airline.common.utils.mapper.audit.AuditBaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO extends AuditBaseDto {

    private UUID id;
    private String name;
    private String code;
    private String description;
    private UUID tenantId;
    private Boolean active;
}
