package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.platform.core.dto.master.messaging.MessagePriorityDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/message-priorities")
public class MessagePriorityController extends BaseController<MessagePriorityDTO, Long> {

    protected MessagePriorityController(BaseService<MessagePriorityDTO, Long> service) {
        super(service);
    }
}
