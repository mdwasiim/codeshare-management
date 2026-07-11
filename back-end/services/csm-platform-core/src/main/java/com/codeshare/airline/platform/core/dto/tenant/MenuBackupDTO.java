package com.codeshare.airline.platform.core.dto.tenant;

import lombok.Data;

@Data
public class MenuBackupDTO {
    private String code;

    private String parentCode;

    private String label;

    private String topbarLabel;

    private String sidebarLabel;

    private String icon;

    private String route;

    private String permission;

    private Integer displayOrder;

    private Boolean visible;

}
