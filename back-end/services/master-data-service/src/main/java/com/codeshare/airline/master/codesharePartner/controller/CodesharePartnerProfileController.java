package com.codeshare.airline.master.codesharepartner.controller;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerProfileDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/codeshare-partner-profiles")
public class CodesharePartnerProfileController extends BaseController<CodesharePartnerProfileDTO, UUID> {
    protected CodesharePartnerProfileController(BaseService<CodesharePartnerProfileDTO, UUID> service) {
        super(service);
    }
}
