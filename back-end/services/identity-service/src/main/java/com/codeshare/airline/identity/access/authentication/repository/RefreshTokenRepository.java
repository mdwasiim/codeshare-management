package com.codeshare.airline.identity.access.authentication.repository;

import com.codeshare.airline.identity.access.authentication.entities.RefreshToken;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CSMDataBaseRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String hashRefreshToken);
}
