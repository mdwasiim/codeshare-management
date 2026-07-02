package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.core.dto.master.schedule.ScheduleSourceDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/schedule-sources")
public class ScheduleSourceController extends BaseController<ScheduleSourceDTO, UUID> {

    protected ScheduleSourceController(BaseService<ScheduleSourceDTO, UUID> service) {
        super(service);
    }
}
