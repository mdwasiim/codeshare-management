package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.core.dto.master.messaging.MessageStatusDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/message-statuses")
public class MessageStatusController extends BaseController<MessageStatusDTO, UUID> {

    protected MessageStatusController(BaseService<MessageStatusDTO, UUID> service) {
        super(service);
    }
}
