package com.codeshare.airline.core.enums.auth;

public enum AuthSource {

    INTERNAL,   // Username/password in your system
    LDAP,       // Corporate directory
    AZURE,      // Azure AD (OIDC)
    KEYCLOAK,
    OKTA,
    OIDC_GENERIC;

    public boolean isOidc() {
        return this == AZURE
                || this == KEYCLOAK
                || this == OKTA
                || this == OIDC_GENERIC;
    }
}
