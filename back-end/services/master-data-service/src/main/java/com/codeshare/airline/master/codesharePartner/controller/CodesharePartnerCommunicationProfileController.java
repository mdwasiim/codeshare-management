package com.codeshare.airline.master.codesharepartner.controller;

import com.codeshare.airline.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/codeshare-partner-communication-profiles")
public class CodesharePartnerCommunicationProfileController extends BaseController<CodesharePartnerCommunicationProfileDTO, UUID> {
    protected CodesharePartnerCommunicationProfileController(BaseService<CodesharePartnerCommunicationProfileDTO, UUID> service) {
        super(service);
    }
}
