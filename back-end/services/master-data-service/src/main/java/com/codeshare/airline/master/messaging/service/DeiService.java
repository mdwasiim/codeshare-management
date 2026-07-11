package com.codeshare.airline.master.messaging.service;

import com.codeshare.airline.platform.core.dto.master.messaging.DeiDTO;
import com.codeshare.airline.master.common.base.BaseService;

import java.util.Optional;
import java.util.UUID;

public interface DeiService
        extends BaseService<DeiDTO, UUID> {

    Optional<DeiDTO> getByDeiNumber(String deiNumber);
}