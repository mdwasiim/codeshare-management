package com.codeshare.airline.platform.core.dto.tenant;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
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
    private RecordStatus recordStatus;
    private boolean primaryAdmin;
}
