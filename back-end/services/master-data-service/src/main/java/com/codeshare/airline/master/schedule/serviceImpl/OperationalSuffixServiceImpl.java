package com.codeshare.airline.master.schedule.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.schedule.OperationalSuffixDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.schedule.entities.OperationalSuffix;
import com.codeshare.airline.master.schedule.mappers.OperationalSuffixMapper;
import com.codeshare.airline.master.schedule.repository.OperationalSuffixRepository;
import com.codeshare.airline.master.schedule.service.OperationalSuffixService;
import org.springframework.stereotype.Service;


@Service
public class OperationalSuffixServiceImpl
        extends BaseServiceImpl<OperationalSuffix, OperationalSuffixDTO, Long>
        implements OperationalSuffixService {

    public OperationalSuffixServiceImpl(OperationalSuffixRepository repository,
                                        OperationalSuffixMapper mapper) {
        super(repository, mapper);
    }
}
