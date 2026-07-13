package com.codeshare.airline.identity.access.authentication.repository;

import com.codeshare.airline.identity.access.authentication.entities.UserDeviceEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository extends CSMDataBaseRepository<UserDeviceEntity, Long> {

    List<UserDeviceEntity> findByUser_Id(Long userId);

    Optional<UserDeviceEntity> findByUser_IdAndDeviceId(Long id, String deviceId);
}
