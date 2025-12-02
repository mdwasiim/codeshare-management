package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.authToken.PasswordRefreshToken;
import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.common.jpa.audit.BaseRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends BaseRepository<PasswordRefreshToken, Long> {
    Optional<PasswordRefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
