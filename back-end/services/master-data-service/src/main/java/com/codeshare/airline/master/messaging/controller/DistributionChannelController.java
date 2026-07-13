package com.codeshare.airline.master.messaging.controller;

import com.codeshare.airline.platform.core.dto.master.messaging.DistributionChannelDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/distribution-channels")
public class DistributionChannelController
        extends BaseController<DistributionChannelDTO, Long> {

    protected DistributionChannelController(BaseService<DistributionChannelDTO, Long> service) {
        super(service);
    }
}