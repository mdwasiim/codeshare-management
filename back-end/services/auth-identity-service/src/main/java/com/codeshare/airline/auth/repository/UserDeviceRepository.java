package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.identity.UserDevice;
import com.codeshare.airline.common.services.jpa.BaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDeviceRepository extends BaseRepository<UserDevice, UUID> {

    List<UserDevice> findByUserId(UUID userId);

    Optional<UserDevice> findByUserIdAndDeviceId(UUID id, String deviceId);
}
