package com.codeshare.airline.platform.core.dto.tenant;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TenantUserDTO extends CSMAuditableDTO {

    private Long tenantId;
    private Long userId;
    private RecordStatus recordStatus;
    private boolean primaryAdmin;
}
