package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.core.dto.master.schedule.ScheduleTypeDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/schedule-types")
public class ScheduleTypeController
        extends BaseController<ScheduleTypeDTO, UUID> {

    protected ScheduleTypeController(BaseService<ScheduleTypeDTO, UUID> service) {
        super(service);
    }
}
