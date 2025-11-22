package com.codeshare.airline.common.auth.model;


import lombok.Data;

import java.util.UUID;

@Data
public class UserRoleDTO {
    private UUID id;
    private UUID userId;
    private UUID roleId;
}
