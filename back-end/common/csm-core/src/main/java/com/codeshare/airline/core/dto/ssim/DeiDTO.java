package com.codeshare.airline.core.dto.ssim;

import com.codeshare.airline.core.enums.schedule.DeiCategory;
import com.codeshare.airline.core.enums.common.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeiDTO {

    private UUID id;

    private String deiNumber;
    private String deiName;
    private String description;

    private DeiCategory deiCategory;

    private Status statusCode;
}