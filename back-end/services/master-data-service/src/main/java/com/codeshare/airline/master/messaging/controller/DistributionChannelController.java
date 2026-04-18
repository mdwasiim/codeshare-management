package com.codeshare.airline.messaging.controller;

import com.codeshare.airline.dto.ssim.DistributionChannelDTO;
import com.codeshare.airline.master.common.base.BaseController;
import com.codeshare.airline.master.common.base.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/distribution-channels")
public class DistributionChannelController
        extends BaseController<DistributionChannelDTO, UUID> {

    protected DistributionChannelController(BaseService<DistributionChannelDTO, UUID> service) {
        super(service);
    }
}