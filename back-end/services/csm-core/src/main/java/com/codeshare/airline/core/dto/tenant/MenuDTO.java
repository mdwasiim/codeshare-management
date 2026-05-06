package com.codeshare.airline.core.dto.tenant;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MenuDTO extends CSMAuditableDTO {

    private String code;

    private String parentCode;

    private String label;

    private String icon;

    private String route;

    private String permission;

    private Integer displayOrder;

    private Boolean visible;

    private UUID parentId;

    private List<UUID> groupIds;
}
