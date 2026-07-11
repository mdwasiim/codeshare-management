package com.codeshare.airline.identity.access.authentication.repository;

import com.codeshare.airline.identity.access.authentication.entities.UserDeviceEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDeviceRepository extends CSMDataBaseRepository<UserDeviceEntity, UUID> {

    List<UserDeviceEntity> findByUser_Id(UUID userId);

    Optional<UserDeviceEntity> findByUser_IdAndDeviceId(UUID id, String deviceId);
}
