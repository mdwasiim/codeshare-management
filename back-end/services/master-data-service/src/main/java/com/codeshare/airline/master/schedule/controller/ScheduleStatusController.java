package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.core.dto.master.schedule.ScheduleStatusDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/schedule-statuses")
public class ScheduleStatusController extends BaseController<ScheduleStatusDTO, UUID> {

    protected ScheduleStatusController(BaseService<ScheduleStatusDTO, UUID> service) {
        super(service);
    }
}
