package com.connect.authentication.infrastructure.security.filter;

import com.connect.authentication.domain.model.LoginResponse;
import com.connect.authentication.infrastructure.security.contract.SecurityUserAdapter;
import com.connect.session.domain.model.DeviceInfoModel;
import com.connect.authentication.infrastructure.security.service.SessionAuthenticationService;
import com.connect.authentication.infrastructure.security.cookie.CookieUtil;
import com.connect.authentication.infrastructure.security.cookie.CookieType;
import com.connect.authentication.domain.model.UsernamePasswordLoginRequest;
import com.connect.user.application.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
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
            String errorMessage = "Request data for authentication is not correct";
            throw new BadCredentialsException(errorMessage, e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String refreshToken = authenticationService.generateRandomToken();
        String accessToken = authenticationService.generateRandomToken();

        Cookie refreshTokenCookie = CookieUtil.createCookieForType(CookieType.REFRESH_TOKEN, refreshToken);

        SecurityUserAdapter authenticatedUser = (SecurityUserAdapter) authResult.getPrincipal();
        authenticationService.createNewSession(
                refreshToken,
                accessToken,
                authenticatedUser.getUser(),
                (DeviceInfoModel) authResult.getDetails()
        );

        LoginResponse loginResponse = new LoginResponse(
                accessToken, UserMapper.INSTANCE.userToUserDto(authenticatedUser.getUser())
        );

        response.setStatus(200);
        response.addCookie(refreshTokenCookie);

        try{
            OutputStream out = response.getOutputStream();
            mapper.writeValue(out, loginResponse);
        }catch (IOException e){
            e.printStackTrace();
        }

        log.info("Authentication successful on UsernamePasswordAuthenticationFilter");
    }
}
