package com.codeshare.airline.identity.access.authentication.repository;


import com.codeshare.airline.identity.access.authentication.entities.AuthTokenExchangeEntity;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthTokenExchangeRepository extends CSMDataBaseRepository<AuthTokenExchangeEntity, Long> {

    Optional<AuthTokenExchangeEntity> findByExchangeCode(String exchangeCode);

    void deleteByExpiresAtBefore(LocalDateTime expiresAt);
}
