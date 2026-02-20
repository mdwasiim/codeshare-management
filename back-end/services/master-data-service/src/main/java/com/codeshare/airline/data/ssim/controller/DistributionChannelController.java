package com.codeshare.airline.data.ssim.controller;

import com.codeshare.airline.core.dto.ssim.DistributionChannelDTO;
import com.codeshare.airline.data.common.BaseController;
import com.codeshare.airline.persistence.persistence.service.BaseService;
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