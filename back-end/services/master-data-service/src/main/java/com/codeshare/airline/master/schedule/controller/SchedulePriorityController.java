package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.schedule.SchedulePriorityDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/schedule-priorities")
public class SchedulePriorityController extends BaseController<SchedulePriorityDTO, Long> {

    protected SchedulePriorityController(BaseService<SchedulePriorityDTO, Long> service) {
        super(service);
    }
}
