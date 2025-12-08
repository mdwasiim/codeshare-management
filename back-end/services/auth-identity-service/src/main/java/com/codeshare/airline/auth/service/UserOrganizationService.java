package com.codeshare.airline.auth.service;


import com.codeshare.airline.common.auth.identity.model.UserOrganizationDTO;

import java.util.List;
import java.util.UUID;

public interface UserOrganizationService {

    UserOrganizationDTO assignUserToOrganization(UserOrganizationDTO dto);

    List<UserOrganizationDTO> getOrganizationsByUser(UUID userId);

    List<UserOrganizationDTO> getUsersByOrganization(UUID organizationId);

    UserOrganizationDTO setPrimary(UUID mappingId);

    void removeUserFromOrganization(UUID mappingId);
}
