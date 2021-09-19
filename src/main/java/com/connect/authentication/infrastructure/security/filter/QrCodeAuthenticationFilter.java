package com.connect.authentication.infrastructure.security.filter;

import com.connect.authentication.domain.model.LoginResponse;
import com.connect.authentication.infrastructure.security.token.QrCodeAuthenticationToken;
import com.connect.authentication.infrastructure.security.contract.SecurityUserAdapter;
import com.connect.authentication.infrastructure.security.service.SessionAuthenticationService;
import com.connect.authentication.infrastructure.security.cookie.CookieUtil;
import com.connect.authentication.infrastructure.security.cookie.CookieType;
import com.connect.session.domain.model.DeviceInfoModel;
import com.connect.shared.exception.application.QrCodeRequestParseException;
import com.connect.authentication.domain.model.QrLoginRequest;
import com.connect.user.application.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
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
                           loginRequest.getMobileId(),
                           loginRequest.getBrowserId()
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
        String refreshToken = authenticationService.generateRandomToken();
        String accessToken = authenticationService.generateRandomToken();

        Cookie refreshTokenCookie = CookieUtil.createCookieForType(
                CookieType.REFRESH_TOKEN, refreshToken
        );

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

        try {
            OutputStream out = response.getOutputStream();
            objectMapper.writeValue(out, loginResponse);
        }catch (IOException e){
            e.printStackTrace();
        }

        response.setStatus(204);
        response.addCookie(refreshTokenCookie);
        log.info("Authentication successful on QrCodeAuthenticationFilter");
    }
}
