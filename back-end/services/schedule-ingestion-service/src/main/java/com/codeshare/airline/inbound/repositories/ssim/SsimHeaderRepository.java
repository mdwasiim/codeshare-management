package com.codeshare.airline.inbound.repositories.ssim;

import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;
import com.codeshare.airline.inbound.entities.ssim.SsimHeaderEntity;

import java.util.UUID;

public interface SsimHeaderRepository
        extends CSMDataBaseRepository<SsimHeaderEntity, UUID> {

}
