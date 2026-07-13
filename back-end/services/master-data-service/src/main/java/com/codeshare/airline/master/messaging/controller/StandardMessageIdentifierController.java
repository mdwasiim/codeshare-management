package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.platform.core.dto.master.messaging.StandardMessageIdentifierDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/standard-message-identifiers")
public class StandardMessageIdentifierController
        extends BaseController<StandardMessageIdentifierDTO, Long> {

    protected StandardMessageIdentifierController(BaseService<StandardMessageIdentifierDTO, Long> service) {
        super(service);
    }
}
