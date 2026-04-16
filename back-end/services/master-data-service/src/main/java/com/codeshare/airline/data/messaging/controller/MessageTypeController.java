package com.codeshare.airline.data.messaging.controller;

import com.codeshare.airline.core.dto.ssim.ScheduleTypeDTO;
import com.codeshare.airline.data.common.BaseController;
import com.codeshare.airline.persistence.persistence.service.BaseService;
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