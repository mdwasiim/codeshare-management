package com.codeshare.airline.repository;

import com.codeshare.airline.entities.RefreshToken;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends CSMDataBaseRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String hashRefreshToken);
}
