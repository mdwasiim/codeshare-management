package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.platform.core.dto.master.messaging.DataElementIdentifierDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/data-element-identifiers")
public class DataElementIdentifierController
        extends BaseController<DataElementIdentifierDTO, UUID> {

    protected DataElementIdentifierController(BaseService<DataElementIdentifierDTO, UUID> service) {
        super(service);
    }
}
