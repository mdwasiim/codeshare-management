package com.codeshare.airline.common.auth.model;


import lombok.Data;

import java.util.UUID;

@Data
public class PermissionRoleDTO {
    private UUID id;
    private UUID permissionId;
    private UUID roleId;
}
