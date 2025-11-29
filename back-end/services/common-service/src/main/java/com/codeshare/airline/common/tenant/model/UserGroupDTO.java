package com.codeshare.airline.common.tenant.model;


import com.codeshare.airline.common.audit.AuditBaseDto;
import com.codeshare.airline.common.audit.AuditInfo;
import lombok.Data;

import java.util.UUID;

@Data
public class UserGroupDTO extends AuditBaseDto {
    private UUID id;
    private String name;
    private String code;
    private String description;
    private Boolean active;
    private UUID tenantId;
    private UUID organizationId;

}
