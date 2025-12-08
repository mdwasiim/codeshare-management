package com.codeshare.airline.auth.service;

import com.codeshare.airline.common.auth.identity.model.UserDTO;
import com.codeshare.airline.common.auth.identity.model.UserDeviceDTO;

import java.util.List;
import java.util.UUID;

public interface UserDeviceService {

    UserDeviceDTO create(UserDeviceDTO dto);

    UserDeviceDTO update(UUID id, UserDeviceDTO dto);

    List<UserDeviceDTO> getDevicesByUserId(java.util.UUID userId);

    UserDeviceDTO registerDevice(UserDTO user, UserDeviceDTO request);

    UserDeviceDTO updateTrust(UserDTO user, String deviceId, boolean trusted);

    void deleteDevice(UserDTO user, String deviceId);

    UserDeviceDTO findOrRegisterDevice(UUID id, UUID tenantId, String deviceId);
}
