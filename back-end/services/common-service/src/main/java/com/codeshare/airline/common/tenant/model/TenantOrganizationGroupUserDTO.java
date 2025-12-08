package com.codeshare.airline.common.tenant.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TenantOrganizationGroupUserDTO {

    private UUID id;

    private UUID groupId;
    private UUID userId;
}
