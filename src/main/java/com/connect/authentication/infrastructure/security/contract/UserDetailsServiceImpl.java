package com.connect.authentication.infrastructure.security.contract;

import com.connect.user.application.service.PostgresUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PostgresUserService postgresUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return postgresUserService.findByUsername(username)
                .map(SecurityUserAdapter::new)
                .orElseThrow(() -> {
                    String errorMessage = "Could not find user with username " + username;
                    return new UsernameNotFoundException(errorMessage);
                });
    }
}
