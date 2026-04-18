package com.codeshare.airline.inbound.repositories.ssim;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.inbound.entities.ssim.SsimCarrierEntity;

import java.util.UUID;

public interface SsimCarrierRepository
        extends CSMDataBaseRepository<SsimCarrierEntity, UUID> {

}
