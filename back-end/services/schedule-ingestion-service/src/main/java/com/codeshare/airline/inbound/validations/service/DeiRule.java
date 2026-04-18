package com.codeshare.airline.inbound.validations.service;

import com.codeshare.airline.inbound.dto.schedule.ScheduleDataElementDTO;
import com.codeshare.airline.inbound.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.inbound.validations.model.ValidationResult;

public interface DeiRule {

    int getCode();

    void validate(ScheduleDataElementDTO dei, ScheduleMessageDTO block, ValidationResult result);
}