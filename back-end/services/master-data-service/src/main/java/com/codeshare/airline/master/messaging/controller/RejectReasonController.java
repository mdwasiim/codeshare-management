package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.core.dto.ssim.RejectReasonDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/reject-reasons")
public class RejectReasonController extends BaseController<RejectReasonDTO, UUID> {

    protected RejectReasonController(BaseService<RejectReasonDTO, UUID> service) {
        super(service);
    }
}
