package com.codeshare.airline.auth.authentication.provider;

import com.codeshare.airline.auth.authentication.api.request.LoginRequest;
import com.codeshare.airline.auth.authentication.service.core.AuthenticationResult;
import com.codeshare.airline.core.enums.auth.AuthSource;

public interface AuthenticationProvider {

    /**
     * Get supported AuthSource
     */
    AuthSource getAuthSource();

    /**
     * Perform authentication
     */
    AuthenticationResult authenticate(LoginRequest request);

}



