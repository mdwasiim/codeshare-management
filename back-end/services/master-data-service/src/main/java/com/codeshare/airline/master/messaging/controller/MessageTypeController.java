package com.codeshare.airline.messaging.controller;

import com.codeshare.airline.dto.ssim.ScheduleTypeDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/message-types")
public class MessageTypeController
        extends BaseController<ScheduleTypeDTO, UUID> {

    protected MessageTypeController(BaseService<ScheduleTypeDTO, UUID> service) {
        super(service);
    }
}