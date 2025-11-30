package com.codeshare.airline.common.auth.model;


import com.codeshare.airline.common.utils.mapper.audit.AuditBaseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO extends AuditBaseDto {

    private UUID id;
    private String code;
    private String name;
    private String description;
    private UUID tenantId;


}
