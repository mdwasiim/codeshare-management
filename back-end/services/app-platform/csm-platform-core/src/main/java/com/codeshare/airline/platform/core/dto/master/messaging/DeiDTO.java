package com.codeshare.airline.platform.core.dto.master.messaging;

import com.codeshare.airline.platform.core.enums.schedule.DeiCategory;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeiDTO {

    private Long id;

    private String deiNumber;
    private String deiName;
    private String description;

    private DeiCategory deiCategory;

    private RecordStatus recordStatus;
}