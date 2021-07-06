package com.connect.configurations.security.filters;

import com.connect.configurations.security.authenticationtokens.QrCodeAuthenticationToken;
import com.connect.configurations.security.services.SessionAuthenticationService;
import com.connect.configurations.security.utils.cookie.CookieUtil;
import com.connect.configurations.security.utils.cookie.CookieType;
import com.connect.dtos.DeviceInfoDto;
import com.connect.exceptions.applicationexceptions.QrCodeRequestParseException;
import com.connect.models.QrLoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
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

@Slf4j
public class QrCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final AuthenticationManager manager;
    private final SessionAuthenticationService authenticationService;

    private static final RequestMatcher DEFAULT_REQUEST_MATCHER = new AntPathRequestMatcher("/auth/qr/login", "POST");
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QrCodeAuthenticationFilter(
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
            QrLoginRequest loginRequest = objectMapper.readValue(
                    httpServletRequest.getInputStream(), QrLoginRequest.class
            );

            String issuedCode = String.format(
                    "Qr-%s-%s",
                           loginRequest.getMobileSignature(),
                           loginRequest.getWebSignature()
                    );

            QrCodeAuthenticationToken authentication = new QrCodeAuthenticationToken(
                    issuedCode, loginRequest, null
            );

            return manager.authenticate(authentication);
        }catch (IOException e){
            String errorMessage = "Qr code authentication failed";
            throw new QrCodeRequestParseException(errorMessage, e);
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
                refreshToken, authorizationToken ,userDetails.getUsername()
        );

        authenticationService.createNewSession(
                refreshToken,
                userDetails.getUsername(),
                (DeviceInfoDto) authResult.getDetails()
        );

        response.setStatus(204);
        response.addCookie(refreshTokenCookie);
        response.addCookie(authorizationCookie);
    }
}
