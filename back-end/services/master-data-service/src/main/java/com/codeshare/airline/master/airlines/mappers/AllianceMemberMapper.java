package com.codeshare.airline.master.airlines.mappers;

import com.codeshare.airline.platform.core.dto.master.airline.AllianceMemberDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.airlines.entities.AllianceMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface AllianceMemberMapper extends CSMGenericMapper<AllianceMember, AllianceMemberDTO> {
    @Mapping(source = "alliance.id", target = "allianceId")
    @Mapping(source = "airline.id", target = "airlineId")
    AllianceMemberDTO toDTO(AllianceMember entity);

    @Mapping(target = "alliance", ignore = true)
    @Mapping(target = "airline", ignore = true)
    AllianceMember toEntity(AllianceMemberDTO dto);

    @Mapping(target = "alliance", ignore = true)
    @Mapping(target = "airline", ignore = true)
    void updateEntityFromDto(AllianceMemberDTO dto, @MappingTarget AllianceMember entity);
}
