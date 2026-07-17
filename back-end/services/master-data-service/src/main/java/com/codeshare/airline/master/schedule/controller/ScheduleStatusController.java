package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.schedule.ScheduleStatusDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/schedule-statuses")
public class ScheduleStatusController extends BaseController<ScheduleStatusDTO, Long> {

    protected ScheduleStatusController(BaseService<ScheduleStatusDTO, Long> service) {
        super(service);
    }
}
