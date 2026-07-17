package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.schedule.ScheduleTypeDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/schedule-types")
public class ScheduleTypeController
        extends BaseController<ScheduleTypeDTO, Long> {

    protected ScheduleTypeController(BaseService<ScheduleTypeDTO, Long> service) {
        super(service);
    }
}
