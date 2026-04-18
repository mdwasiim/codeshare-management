package com.codeshare.airline.service;

import com.codeshare.airline.core.enums.auth.AuthSource;

public interface AuthIdentityProviderService {
     void assertProviderEnabled(String tenantId, AuthSource source) ;
}
