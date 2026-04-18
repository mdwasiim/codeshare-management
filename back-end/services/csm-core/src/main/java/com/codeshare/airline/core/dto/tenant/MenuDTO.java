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
    private UUID id;

    private String label;

    private String icon;

    private List<String> routerLink;

    private Integer displayOrder;

    private UUID tenantId;

    private UUID parentId;

    private List<MenuDTO> items;

}
