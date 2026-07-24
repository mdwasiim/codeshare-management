package com.codeshare.airline.platform.core.dto.master.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ScheduleTimeValidationRequestDTO {

    private List<ScheduleTimeValidationLegDTO> legs = new ArrayList<>();
}
