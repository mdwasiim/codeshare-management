package com.codeshare.airline.master.aircraft.controller;

import com.codeshare.airline.platform.core.dto.master.aircraft.CabinConfigurationTemplateDTO;
import com.codeshare.airline.master.aircraft.service.CabinConfigurationTemplateService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/cabin-configuration-templates")
public class CabinConfigurationTemplateController
        extends BaseController<CabinConfigurationTemplateDTO, UUID> {

    public CabinConfigurationTemplateController(CabinConfigurationTemplateService service) {
        super(service);
    }
}
