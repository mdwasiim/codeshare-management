package com.codeshare.airline.auth.service;

import com.codeshare.airline.common.auth.model.AuthResponse;

public interface Authservice {
    public AuthResponse getLoginToken(String userName);
    public AuthResponse getTokenFromRefreshToken(String refreshToken);

    public void logout(String refreshToken);


}
