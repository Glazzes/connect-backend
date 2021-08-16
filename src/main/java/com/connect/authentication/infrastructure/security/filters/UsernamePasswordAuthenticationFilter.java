package com.connect.authentication.infrastructure.security.filters;

import com.connect.session.application.dto.DeviceInfoDto;
import com.connect.authentication.infrastructure.security.services.SessionAuthenticationService;
import com.connect.authentication.infrastructure.security.utils.cookie.CookieUtil;
import com.connect.authentication.infrastructure.security.utils.cookie.CookieType;
import com.connect.authentication.domain.model.UsernamePasswordLoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final RequestMatcher DEFAULT_REQUEST_MATCHER = new AntPathRequestMatcher("/auth/login", "POST");
    private final ObjectMapper mapper = new ObjectMapper();

    private final AuthenticationManager manager;
    private final SessionAuthenticationService authenticationService;

    public UsernamePasswordAuthenticationFilter(
            AuthenticationManager manager,
            SessionAuthenticationService authenticationService
    ){
        super(DEFAULT_REQUEST_MATCHER, manager);
        this.manager = manager;
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        try{
            UsernamePasswordLoginRequest loginRequest = mapper
                    .readValue(httpServletRequest.getInputStream(), UsernamePasswordLoginRequest.class);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword(), null
            );

            authentication.setDetails(loginRequest.getDeviceInfo());
            return manager.authenticate(authentication);
        }catch (IOException e){
            e.printStackTrace();
            String errorMessage = "Request data for authentication is not correct";
            throw new BadCredentialsException(errorMessage, e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String refreshToken = UUID.randomUUID().toString();
        String authorizationToken = UUID.randomUUID().toString();

        Cookie refreshTokenCookie = CookieUtil.createCookieForType(
                CookieType.REFRESH_TOKEN, refreshToken
        );

        Cookie authorizationCookie = CookieUtil.createCookieForType(
                CookieType.AUTHORIZATION_TOKEN, authorizationToken
        );

        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        authenticationService.associateRefreshTokenWithAuthorizationToken(
                refreshToken, authorizationToken, userDetails.getUsername()
        );

        authenticationService.createNewSession(
                refreshToken,
                userDetails.getUsername(),
                (DeviceInfoDto) authResult.getDetails());

        response.setStatus(204);
        response.addCookie(refreshTokenCookie);
        response.addCookie(authorizationCookie);
    }
}
