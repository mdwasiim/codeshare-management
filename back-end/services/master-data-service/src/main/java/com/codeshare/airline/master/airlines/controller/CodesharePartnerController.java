package com.codeshare.airline.master.airlines.controller;

import com.codeshare.airline.core.dto.master.airline.CodesharePartnerDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/codeshare-partners")
public class CodesharePartnerController extends BaseController<CodesharePartnerDTO, UUID> {
    protected CodesharePartnerController(BaseService<CodesharePartnerDTO, UUID> service) {
        super(service);
    }
}
