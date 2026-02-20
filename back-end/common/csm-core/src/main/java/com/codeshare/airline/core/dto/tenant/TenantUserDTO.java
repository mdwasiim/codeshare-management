package com.codeshare.airline.core.dto.tenant;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import com.codeshare.airline.core.enums.common.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TenantUserDTO extends CSMAuditableDTO {

    private UUID tenantId;
    private UUID userId;
    private Status status;
    private boolean primaryAdmin;
}
