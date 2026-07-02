package com.codeshare.airline.master.messaging.serviceImpl;

import com.codeshare.airline.core.dto.ssim.RejectReasonDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.messaging.entities.RejectReason;
import com.codeshare.airline.master.messaging.mappers.RejectReasonMapper;
import com.codeshare.airline.master.messaging.repository.RejectReasonRepository;
import com.codeshare.airline.master.messaging.service.RejectReasonService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RejectReasonServiceImpl
        extends BaseServiceImpl<RejectReason, RejectReasonDTO, UUID>
        implements RejectReasonService {

    public RejectReasonServiceImpl(RejectReasonRepository repository,
                                   RejectReasonMapper mapper) {
        super(repository, mapper);
    }
}
