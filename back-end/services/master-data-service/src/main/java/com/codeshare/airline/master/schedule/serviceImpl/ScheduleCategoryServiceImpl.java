package com.codeshare.airline.master.schedule.serviceImpl;

import com.codeshare.airline.platform.core.dto.master.schedule.ScheduleCategoryDTO;
import com.codeshare.airline.master.common.base.BaseServiceImpl;
import com.codeshare.airline.master.schedule.entities.ScheduleCategory;
import com.codeshare.airline.master.schedule.mappers.ScheduleCategoryMapper;
import com.codeshare.airline.master.schedule.repository.ScheduleCategoryRepository;
import com.codeshare.airline.master.schedule.service.ScheduleCategoryService;
import org.springframework.stereotype.Service;


@Service
public class ScheduleCategoryServiceImpl
        extends BaseServiceImpl<ScheduleCategory, ScheduleCategoryDTO, Long>
        implements ScheduleCategoryService {

    public ScheduleCategoryServiceImpl(ScheduleCategoryRepository repository,
                                       ScheduleCategoryMapper mapper) {
        super(repository, mapper);
    }
}
