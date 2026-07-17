package com.codeshare.airline.identity.access.authentication.service;


import com.codeshare.airline.platform.core.dto.auth.UserDeviceDTO;
import com.codeshare.airline.identity.access.identity.entities.User;
import com.codeshare.airline.platform.core.dto.auth.AuthUserDTO;

import java.util.List;

public interface AuthUserDeviceService {

    UserDeviceDTO create(UserDeviceDTO dto);

    UserDeviceDTO update(Long id, UserDeviceDTO dto);

    List<UserDeviceDTO> getDevicesByUserId(Long userId);

    UserDeviceDTO registerDevice(AuthUserDTO user, UserDeviceDTO request);

    UserDeviceDTO updateTrust(AuthUserDTO user, String deviceId, boolean trusted);

    void deleteDevice(AuthUserDTO user, String deviceId);

    UserDeviceDTO findOrRegisterDevice(Long id, Long tenantId, String deviceId);

    void track(User User, String deviceId, String userAgent, String ip);
}
