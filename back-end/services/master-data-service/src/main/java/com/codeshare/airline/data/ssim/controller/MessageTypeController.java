package com.codeshare.airline.data.ssim.controller;

import com.codeshare.airline.core.dto.ssim.MessageTypeDTO;
import com.codeshare.airline.data.common.BaseController;
import com.codeshare.airline.persistence.persistence.service.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/message-types")
public class MessageTypeController
        extends BaseController<MessageTypeDTO, UUID> {

    protected MessageTypeController(BaseService<MessageTypeDTO, UUID> service) {
        super(service);
    }
}