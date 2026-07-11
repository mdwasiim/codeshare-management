package com.codeshare.airline.platform.core.dto.tenant;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PermissionDTO extends CSMAuditableDTO {

    private String name;     // "User Create"

    private String code;            // "USER:CREATE"

    private String description;

    private String domain;          // "USER"
    private String action;          // "CREATE"

    // Optional
    private String tenantCode;

}
