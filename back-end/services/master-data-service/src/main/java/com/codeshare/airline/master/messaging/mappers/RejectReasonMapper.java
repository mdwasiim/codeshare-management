package com.codeshare.airline.master.messaging.mappers;

import com.codeshare.airline.platform.core.dto.master.messaging.RejectReasonDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.messaging.entities.RejectReason;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface RejectReasonMapper extends CSMGenericMapper<RejectReason, RejectReasonDTO> {
}
