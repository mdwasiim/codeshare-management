package com.codeshare.airline.common.auth.model;


import lombok.Data;

import java.util.UUID;

@Data
public class GroupDTO {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private UUID tenantId;
}
