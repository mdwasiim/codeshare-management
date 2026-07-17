package com.codeshare.airline.identity.access.identity.service;



import com.codeshare.airline.identity.access.authentication.core.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.platform.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.platform.core.enums.auth.AuthSource;

import java.util.List;

public interface AuthUserService {

    AuthUserDTO create(AuthUserDTO dto);

    AuthUserDTO update(Long id, AuthUserDTO dto);

    AuthUserDTO getById(Long id);

    void delete(Long id);

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
