package com.codeshare.airline.auth.authentication.security.adapter;


import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Getter
public class UserDetailsAdapter implements UserDetails {

    private final UUID userId;
    private final String username;
    private final UUID tenantId;
    private final String tenantCode;

    private final boolean enabled;
    private final boolean accountNonLocked;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsAdapter(
            UUID userId,
            String username,
            UUID tenantId,
            String tenantCode,
            boolean enabled,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.userId = userId;
        this.username = username;
        this.tenantId = tenantId;
        this.tenantCode = tenantCode;
        this.enabled = enabled;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return ""; // JWT-based auth
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // not tracked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // JWT-based auth
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetailsAdapter that)) return false;
        return userId.equals(that.userId) &&
                tenantId.equals(that.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, tenantId);
    }
}


