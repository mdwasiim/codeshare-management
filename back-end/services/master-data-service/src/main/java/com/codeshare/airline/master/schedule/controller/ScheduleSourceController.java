package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.schedule.ScheduleSourceDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/schedule-sources")
public class ScheduleSourceController extends BaseController<ScheduleSourceDTO, Long> {

    protected ScheduleSourceController(BaseService<ScheduleSourceDTO, Long> service) {
        super(service);
    }
}
