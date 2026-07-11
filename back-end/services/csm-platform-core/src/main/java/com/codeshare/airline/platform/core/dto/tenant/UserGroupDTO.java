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
public class UserGroupDTO extends CSMAuditableDTO {

    private UUID id;

    private UUID userId;

    private UUID groupId;
}
