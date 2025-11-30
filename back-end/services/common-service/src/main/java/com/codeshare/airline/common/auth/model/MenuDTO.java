package com.codeshare.airline.common.auth.model;


import com.codeshare.airline.common.utils.mapper.audit.AuditBaseDto;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class MenuDTO extends AuditBaseDto {
    private UUID id;
    private String name;
    private String code;
    private String url;
    private String icon;
    private String title;

    private Map<String, String> iconComponent;
    private Map<String, String> badge;

    private UUID tenantId;
    private UUID organizationId;

    private UUID parentId;

    private List<MenuDTO> children;

}
