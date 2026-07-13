package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.platform.core.dto.master.messaging.MessageStatusDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/message-statuses")
public class MessageStatusController extends BaseController<MessageStatusDTO, Long> {

    protected MessageStatusController(BaseService<MessageStatusDTO, Long> service) {
        super(service);
    }
}
