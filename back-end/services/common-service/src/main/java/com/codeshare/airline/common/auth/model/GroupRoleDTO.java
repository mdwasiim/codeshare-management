package com.codeshare.airline.common.auth.model;

import lombok.Data;

import java.util.UUID;

@Data
public class GroupRoleDTO {
    private UUID id;
    private UUID groupId;
    private UUID roleId;
}
