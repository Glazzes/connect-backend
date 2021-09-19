package com.connect.authentication.infrastructure.security.provider;

import com.connect.authentication.infrastructure.security.contract.SecurityUserAdapter;
import com.connect.authentication.infrastructure.security.token.SuccessfulAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String authUsername = (String) authentication.getPrincipal();
        String authPassword = (String) authentication.getCredentials();

        SecurityUserAdapter userDetails = (SecurityUserAdapter) userDetailsService.loadUserByUsername(authUsername);
        String userPassword = userDetails.getPassword();

        if(passwordEncoder.matches(authPassword, userPassword)){
            SuccessfulAuthenticationToken successfulAuthentication = new SuccessfulAuthenticationToken(
                    userDetails,
                    userDetails.getPassword(),
                    null
            );

            successfulAuthentication.setDetails(authentication.getDetails());
            return successfulAuthentication;
        }else{
            String errorMessage = "Username or password are incorrect";
            log.error(errorMessage);
            throw new BadCredentialsException(errorMessage);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
