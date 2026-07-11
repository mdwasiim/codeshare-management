package com.codeshare.airline.identity.access.identity.service;



import com.codeshare.airline.identity.access.authentication.core.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.core.enums.auth.AuthSource;

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

    UserDetailsAdapter getAuthUserForFederatedLogin(
            String tenantCode,
            AuthSource authSource,
            String externalId,
            String username,
            String email
    );
}
