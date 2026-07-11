package com.codeshare.airline.identity.access.authentication.service;


import com.codeshare.airline.platform.core.dto.auth.UserDeviceDTO;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.platform.core.dto.auth.AuthUserDTO;

import java.util.List;
import java.util.UUID;

public interface AuthUserDeviceService {

    UserDeviceDTO create(UserDeviceDTO dto);

    UserDeviceDTO update(UUID id, UserDeviceDTO dto);

    List<UserDeviceDTO> getDevicesByUserId(UUID userId);

    UserDeviceDTO registerDevice(AuthUserDTO user, UserDeviceDTO request);

    UserDeviceDTO updateTrust(AuthUserDTO user, String deviceId, boolean trusted);

    void deleteDevice(AuthUserDTO user, String deviceId);

    UserDeviceDTO findOrRegisterDevice(UUID id, UUID tenantId, String deviceId);

    void track(User User, String deviceId, String userAgent, String ip);
}
