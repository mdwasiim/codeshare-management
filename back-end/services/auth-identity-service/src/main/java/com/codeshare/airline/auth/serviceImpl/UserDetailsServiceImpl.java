package com.codeshare.airline.auth.serviceImpl;


import com.codeshare.airline.auth.entities.identity.User;
import com.codeshare.airline.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;

    public UserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("DB Username    = " + user.getUsername());
        System.out.println("DB Password    = " + user.getPassword());
        System.out.println("DB Enabled     = " + user.isEnabled());
        System.out.println("DB Roles       = " + user.getUserRoles());


        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getUserRoles().stream().map(r -> r.getRole().getName()).toArray(String[]::new))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }
}
