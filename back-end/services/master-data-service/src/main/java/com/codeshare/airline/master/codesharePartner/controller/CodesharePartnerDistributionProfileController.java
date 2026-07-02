package com.codeshare.airline.master.codesharepartner.controller;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/codeshare-partner-distribution-profiles")
public class CodesharePartnerDistributionProfileController extends BaseController<CodesharePartnerDistributionProfileDTO, UUID> {
    protected CodesharePartnerDistributionProfileController(BaseService<CodesharePartnerDistributionProfileDTO, UUID> service) {
        super(service);
    }
}
