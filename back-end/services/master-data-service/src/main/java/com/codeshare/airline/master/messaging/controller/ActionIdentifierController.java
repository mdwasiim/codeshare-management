package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.platform.core.dto.master.messaging.ActionIdentifierDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/action-identifiers")
public class ActionIdentifierController
        extends BaseController<ActionIdentifierDTO, UUID> {

    protected ActionIdentifierController(BaseService<ActionIdentifierDTO, UUID> service) {
        super(service);
    }
}