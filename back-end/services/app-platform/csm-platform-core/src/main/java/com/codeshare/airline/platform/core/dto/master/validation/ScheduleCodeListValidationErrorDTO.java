package com.codeshare.airline.platform.core.dto.master.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCodeListValidationErrorDTO {

    private String field;
    private String code;
    private String message;
}
