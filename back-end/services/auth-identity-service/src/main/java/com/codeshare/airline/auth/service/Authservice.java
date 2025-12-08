package com.codeshare.airline.auth.service;

import com.codeshare.airline.common.auth.identity.model.AuthResponse;

public interface Authservice {
    public AuthResponse getLoginToken(String userName, String deviceId, String userAgent, String ip);
    public AuthResponse getTokenFromRefreshToken(String refreshToken, String deviceId, String userAgent, String ip);

    public void logout(String refreshToken);


}
