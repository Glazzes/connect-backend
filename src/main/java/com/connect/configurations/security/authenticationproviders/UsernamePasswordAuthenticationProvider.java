package com.connect.configurations.security.authenticationproviders;

import com.connect.configurations.security.authenticationtokens.SuccessfulAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationProvider(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ){
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String authUsername = (String) authentication.getPrincipal();
        String authPassword = (String) authentication.getCredentials();

        UserDetails userDetails = userDetailsService.loadUserByUsername(authUsername);
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
            throw new BadCredentialsException(errorMessage);
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
