package com.codeshare.airline.common.tenant.model;


import lombok.Data;

import java.util.UUID;

@Data
public class UserGroupDTO {
    private UUID id;
    private String name;
    private String code;
    private String description;
    private Boolean active;
    private UUID tenantId;
    private UUID organizationId;
}
