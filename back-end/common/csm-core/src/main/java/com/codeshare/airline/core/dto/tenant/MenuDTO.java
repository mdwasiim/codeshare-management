package com.codeshare.airline.core.dto.tenant;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;
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

    private String name;
    private String code;
    private String url;
    private String icon;
    private String title;

    private Map<String, String> iconComponent;
    private Map<String, String> badge;
    private Map<String, String> attributes;

    private UUID tenantId;
    private UUID organizationId;

    private UUID parentId;

    private List<MenuDTO> children;

}
