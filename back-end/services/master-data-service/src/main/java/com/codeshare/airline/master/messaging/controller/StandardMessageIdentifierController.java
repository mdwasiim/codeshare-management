package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.core.dto.master.messaging.StandardMessageIdentifierDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/standard-message-identifiers")
public class StandardMessageIdentifierController
        extends BaseController<StandardMessageIdentifierDTO, UUID> {

    protected StandardMessageIdentifierController(BaseService<StandardMessageIdentifierDTO, UUID> service) {
        super(service);
    }
}
