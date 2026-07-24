package com.codeshare.airline.platform.core.dto.tenant;

import lombok.Data;
import com.codeshare.airline.platform.core.enums.tenant.MenuNavigationType;

@Data
public class MenuBackupDTO {
    private String code;

    private String parentCode;

    private String label;

    private String topbarLabel;

    private String sidebarLabel;

    private String icon;

    private MenuNavigationType navigationType;

    private String frontendPath;

    private String externalUrl;

    private String permissionCode;

    private Integer displayOrder;

    private Boolean visible;

}
