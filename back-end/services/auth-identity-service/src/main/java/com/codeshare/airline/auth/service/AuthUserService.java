package com.codeshare.airline.auth.service;



import com.codeshare.airline.auth.authentication.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.core.dto.auth.AuthUserDTO;

import java.util.List;
import java.util.UUID;

public interface AuthUserService {

    AuthUserDTO create(AuthUserDTO dto);

    AuthUserDTO update(UUID id, AuthUserDTO dto);

    AuthUserDTO getById(UUID id);

    void delete(UUID id);

    AuthUserDTO getByUsername(String name);

    List<AuthUserDTO> getAllUsers();

    UserDetailsAdapter getAuthUserByUsername(String name);
}
