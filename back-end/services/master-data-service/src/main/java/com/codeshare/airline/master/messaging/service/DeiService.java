package com.codeshare.airline.messaging.service;

import com.codeshare.airline.dto.ssim.DeiDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.Optional;
import java.util.UUID;

public interface DeiService
        extends BaseService<DeiDTO, UUID> {

    Optional<DeiDTO> getByDeiNumber(String deiNumber);
}