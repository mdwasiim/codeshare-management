package com.codeshare.airline.repository;

import com.codeshare.airline.identity.entities.AuthTokenExchangeEntity;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface AuthTokenExchangeRepository extends CSMDataBaseRepository<AuthTokenExchangeEntity, UUID> {

    Optional<AuthTokenExchangeEntity> findByExchangeCode(String exchangeCode);

    void deleteByExpiresAtBefore(LocalDateTime expiresAt);
}
