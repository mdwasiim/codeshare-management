package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.AuthTokenExchangeEntity;
import com.codeshare.airline.persistence.repository.CSMDataBaseRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface AuthTokenExchangeRepository extends CSMDataBaseRepository<AuthTokenExchangeEntity, UUID> {

    Optional<AuthTokenExchangeEntity> findByExchangeCode(String exchangeCode);

    void deleteByExpiresAtBefore(LocalDateTime expiresAt);
}
