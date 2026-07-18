package com.codeshare.airline.platform.core.dto.master.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTimeValidationErrorDTO {

    private String field;
    private String message;
}
