package com.codeshare.airline.identity.access.authentication.repository;


import com.codeshare.airline.identity.access.authentication.entities.AuthTokenExchangeEntity;
import com.codeshare.airline.data.repository.CSMDataBaseRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface AuthTokenExchangeRepository extends CSMDataBaseRepository<AuthTokenExchangeEntity, UUID> {

    Optional<AuthTokenExchangeEntity> findByExchangeCode(String exchangeCode);

    void deleteByExpiresAtBefore(LocalDateTime expiresAt);
}
