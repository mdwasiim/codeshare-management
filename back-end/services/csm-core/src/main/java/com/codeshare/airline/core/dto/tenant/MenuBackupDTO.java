package com.codeshare.airline.core.dto.tenant;

import lombok.Data;

@Data
public class MenuBackupDTO {
    private String code;

    private String parentCode;

    private String label;

    private String icon;

    private String route;

    private String permission;

    private Integer displayOrder;

    private Boolean visible;

}
