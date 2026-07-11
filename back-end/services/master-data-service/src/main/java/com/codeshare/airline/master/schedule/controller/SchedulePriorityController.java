package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.platform.core.dto.master.schedule.SchedulePriorityDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/schedule-priorities")
public class SchedulePriorityController extends BaseController<SchedulePriorityDTO, UUID> {

    protected SchedulePriorityController(BaseService<SchedulePriorityDTO, UUID> service) {
        super(service);
    }
}
