package com.codeshare.airline.auth.repository;

import com.codeshare.airline.auth.entities.authToken.PasswordRefreshToken;
import com.codeshare.airline.auth.entities.identity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<PasswordRefreshToken, Long> {
    Optional<PasswordRefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
