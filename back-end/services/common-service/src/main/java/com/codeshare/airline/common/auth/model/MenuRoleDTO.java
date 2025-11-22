package com.codeshare.airline.common.auth.model;


import lombok.Data;

import java.util.UUID;

@Data
public class MenuRoleDTO {
    private UUID id;
    private UUID menuId;
    private UUID roleId;
}
