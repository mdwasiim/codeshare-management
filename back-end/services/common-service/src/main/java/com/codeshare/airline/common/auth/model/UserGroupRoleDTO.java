package com.codeshare.airline.common.auth.model;


import com.codeshare.airline.common.utils.mapper.audit.AuditBaseDto;
import lombok.Data;

import java.util.UUID;

@Data
public class UserGroupRoleDTO extends AuditBaseDto {
    private UUID id;
    private UUID userId;
    private UUID groupId;
    private UUID roleId;

}
