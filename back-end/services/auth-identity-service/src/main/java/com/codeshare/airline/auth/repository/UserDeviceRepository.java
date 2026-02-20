package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.UserDeviceEntity;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDeviceRepository extends CSMDataBaseRepository<UserDeviceEntity, UUID> {

    List<UserDeviceEntity> findByUser_Id(UUID userId);

    Optional<UserDeviceEntity> findByUser_IdAndDeviceId(UUID id, String deviceId);
}
