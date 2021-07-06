package com.connect.configurations.security.services;

import com.connect.dtos.DeviceInfoDto;
import com.connect.entities.redis.RedisSession;
import com.connect.repositories.redis.RedisSessionRepository;
import com.connect.services.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionAuthenticationService {
    private final SessionService sessionService;
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
