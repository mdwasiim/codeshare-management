package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.platform.core.dto.master.messaging.DataElementIdentifierDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/data-element-identifiers")
public class DataElementIdentifierController
        extends BaseController<DataElementIdentifierDTO, Long> {

    protected DataElementIdentifierController(BaseService<DataElementIdentifierDTO, Long> service) {
        super(service);
    }
}
