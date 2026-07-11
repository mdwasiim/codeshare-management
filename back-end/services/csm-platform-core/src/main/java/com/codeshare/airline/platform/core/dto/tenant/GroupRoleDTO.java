package com.codeshare.airline.platform.core.dto.tenant;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
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
public class GroupRoleDTO extends CSMAuditableDTO {
    private UUID id;
    private UUID groupId;
    private UUID roleId;
}
