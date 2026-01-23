package com.codeshare.airline.auth.service;


import com.codeshare.airline.auth.model.entities.User;
import com.codeshare.airline.core.dto.auth.AuthUserDTO;
import com.codeshare.airline.core.dto.auth.AuthUserDeviceDTO;

import java.util.List;
import java.util.UUID;

public interface AuthUserDeviceService {

    AuthUserDeviceDTO create(AuthUserDeviceDTO dto);

    AuthUserDeviceDTO update(UUID id, AuthUserDeviceDTO dto);

    List<AuthUserDeviceDTO> getDevicesByUserId(UUID userId);

    AuthUserDeviceDTO registerDevice(AuthUserDTO user, AuthUserDeviceDTO request);

    AuthUserDeviceDTO updateTrust(AuthUserDTO user, String deviceId, boolean trusted);

    void deleteDevice(AuthUserDTO user, String deviceId);

    AuthUserDeviceDTO findOrRegisterDevice(UUID id, UUID tenantId, String deviceId);

    void track(User User, String deviceId, String userAgent, String ip);
}
