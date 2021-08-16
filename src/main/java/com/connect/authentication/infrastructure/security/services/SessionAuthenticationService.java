package com.connect.authentication.infrastructure.security.services;

import com.connect.session.application.dto.DeviceInfoDto;
import com.connect.session.domain.entities.RedisSession;
import com.connect.session.domain.repository.RedisSessionRepository;
import com.connect.session.application.service.PostgresSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionAuthenticationService {
    private final PostgresSessionService sessionService;
    private final UserDetailsService userDetailsService;
    private final RedisSessionRepository redisSessionRepository;

    public UserDetails getUserDetailsByRefreshToken(String refreshToken){
        String username = redisSessionRepository.findById(refreshToken)
                .map(RedisSession::getOwner)
                .orElseThrow(() -> new IllegalArgumentException("bad"));

        return userDetailsService.loadUserByUsername(username);
    }

    public void createNewSession(String refreshToken, String username, DeviceInfoDto deviceInfo){
        sessionService.saveSession(refreshToken, username, deviceInfo);
    }

    public void associateRefreshTokenWithAuthorizationToken(
            String refreshToken,
            String authorizationToken,
            String username){
        RedisSession newSession = new RedisSession(
            refreshToken,
            authorizationToken,
            username,
            true,
            true
        );

        redisSessionRepository.save(newSession);
    }

    public RedisSession findRedisSessionByRefreshToken(String refreshToken){
        return redisSessionRepository.findById(refreshToken)
                .orElseThrow(() -> {
                    String errorMessage = "There's no session associated with these refreshToken";
                    return new IllegalArgumentException(errorMessage);
                });
    }

}
