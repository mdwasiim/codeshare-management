package com.codeshare.airline.master.schedule.controller;

import com.codeshare.airline.core.dto.master.schedule.ScheduleChannelDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/schedule-channels")
public class ScheduleChannelController extends BaseController<ScheduleChannelDTO, UUID> {

    protected ScheduleChannelController(BaseService<ScheduleChannelDTO, UUID> service) {
        super(service);
    }
}
