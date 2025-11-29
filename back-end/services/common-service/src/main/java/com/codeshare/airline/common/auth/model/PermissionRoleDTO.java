package com.codeshare.airline.common.auth.model;


import com.codeshare.airline.common.audit.AuditBaseDto;
import com.codeshare.airline.common.audit.AuditInfo;
import lombok.Data;

import java.util.UUID;

@Data
public class PermissionRoleDTO extends AuditBaseDto {
    private UUID id;
    private UUID permissionId;
    private UUID roleId;

}
