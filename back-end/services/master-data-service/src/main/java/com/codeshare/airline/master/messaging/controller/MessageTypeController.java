package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.core.dto.ssim.ScheduleTypeDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/message-types")
public class MessageTypeController
        extends BaseController<ScheduleTypeDTO, UUID> {

    protected MessageTypeController(BaseService<ScheduleTypeDTO, UUID> service) {
        super(service);
    }
}