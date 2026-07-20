package com.codeshare.airline.master.common.dto;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;

public record CommonReferenceOptionDTO(
        Long id,
        String categoryCode,
        String value,
        String label,
        String description,
        Integer displayOrder,
        RecordStatus recordStatus,
        Boolean active
) {
}
