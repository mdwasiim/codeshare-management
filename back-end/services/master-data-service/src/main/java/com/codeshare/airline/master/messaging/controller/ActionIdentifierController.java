package com.codeshare.airline.messaging.controller;

import com.codeshare.airline.dto.ssim.ActionIdentifierDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/action-identifiers")
public class ActionIdentifierController
        extends BaseController<ActionIdentifierDTO, UUID> {

    protected ActionIdentifierController(BaseService<ActionIdentifierDTO, UUID> service) {
        super(service);
    }
}