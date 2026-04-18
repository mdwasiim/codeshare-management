package com.codeshare.airline.identity.repository;

import com.codeshare.airline.identity.entities.UserDeviceEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDeviceRepository extends CSMDataBaseRepository<UserDeviceEntity, UUID> {

    List<UserDeviceEntity> findByUser_Id(UUID userId);

    Optional<UserDeviceEntity> findByUser_IdAndDeviceId(UUID id, String deviceId);
}
