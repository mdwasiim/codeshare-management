package com.codeshare.airline.platform.core.dto.tenant;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.platform.core.enums.tenant.MenuNavigationType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

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

    private String topbarLabel;

    private String sidebarLabel;

    private String icon;

    /**
     * SECTION for grouping-only rows, INTERNAL_LINK for Angular routes,
     * EXTERNAL_LINK for links outside the app.
     */
    private MenuNavigationType navigationType;

    /**
     * Angular route path. This is intentionally stored because the UI
     * receives tenant/group-specific menus from identity-service.
     */
    private String frontendPath;

    private String externalUrl;

    /**
     * Permission code required before identity-service returns this menu item.
     */
    private String permissionCode;

    private Integer displayOrder;

    private Boolean visible;

    private Long parentId;

    private List<Long> groupIds;
}
