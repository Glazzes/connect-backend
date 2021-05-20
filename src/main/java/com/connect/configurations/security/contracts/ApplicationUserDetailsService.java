package com.connect.configurations.security.contracts;

import com.connect.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findByUsername(username)
                .map(ApplicationUserDetails::new)
                .orElseThrow(() -> {
                    String errorMessage = "Could not find user with username " + username;
                    return new UsernameNotFoundException(errorMessage);
                });
    }
}
