package com.codeshare.airline.common.auth.model;


import com.codeshare.airline.common.audit.AuditBaseDto;
import com.codeshare.airline.common.audit.AuditInfo;
import lombok.Data;

import java.util.UUID;

@Data
public class MenuRoleDTO extends AuditBaseDto {
    private UUID id;
    private UUID menuId;
    private UUID roleId;

}
