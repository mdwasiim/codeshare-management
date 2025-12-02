package com.codeshare.airline.common.tenant.model;

import com.codeshare.airline.common.utils.mapper.audit.AuditBaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO extends AuditBaseDto {

    private UUID id;

    private String name;
    private String code;
    private String description;

    private Boolean active;

    private UUID tenantId;

    private UUID parentId;

    private List<OrganizationDTO> children;
}
