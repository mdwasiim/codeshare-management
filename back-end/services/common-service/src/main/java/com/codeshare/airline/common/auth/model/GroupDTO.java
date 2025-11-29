package com.codeshare.airline.common.auth.model;


import com.codeshare.airline.common.audit.AuditBaseDto;
import lombok.Data;

import java.util.UUID;

@Data
public class GroupDTO extends AuditBaseDto {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private UUID tenantId;

}
