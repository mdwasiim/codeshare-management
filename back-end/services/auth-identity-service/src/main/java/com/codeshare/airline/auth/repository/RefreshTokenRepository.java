package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.authentication.RefreshToken;
import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.entities.identity.UserDevice;
import com.codeshare.airline.common.services.jpa.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends BaseRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshTokenHash(String refreshTokenHash);
    void deleteByUser(User user);

    List<RefreshToken> findByUserDevice(UserDevice userDevice);
}
