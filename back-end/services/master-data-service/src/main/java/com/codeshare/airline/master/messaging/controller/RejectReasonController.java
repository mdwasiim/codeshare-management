package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.platform.core.dto.master.messaging.RejectReasonDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/reject-reasons")
public class RejectReasonController extends BaseController<RejectReasonDTO, Long> {

    protected RejectReasonController(BaseService<RejectReasonDTO, Long> service) {
        super(service);
    }
}
