package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.common.auth.model.UserDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<User, UserDTO> {
}
