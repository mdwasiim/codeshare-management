package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.core.dto.ssim.ActionCodeDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/action-codes")
public class ActionCodeController extends BaseController<ActionCodeDTO, UUID> {

    protected ActionCodeController(BaseService<ActionCodeDTO, UUID> service) {
        super(service);
    }
}
