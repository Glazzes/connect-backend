package com.connect.authentication.infrastructure.security.service;

import com.connect.session.domain.model.DeviceInfoModel;
import com.connect.session.application.service.RedisSessionService;
import com.connect.session.domain.entities.PostgresSession;
import com.connect.session.domain.entities.RedisSession;
import com.connect.session.application.service.PostgresSessionService;
import com.connect.user.domain.entities.PostgresUser;
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
    private final PostgresSessionService postgresSessionService;
    private final RedisSessionService redisSessionService;
    private final UserDetailsService userDetailsService;

    public UserDetails getUserDetailsByRefreshToken(String refreshToken){
        String username = redisSessionService.findById(refreshToken)
                .map(RedisSession::getOwnerUsername)
                .orElseThrow(() -> new IllegalArgumentException("bad"));

        return userDetailsService.loadUserByUsername(username);
    }

    public String generateRandomToken(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public void createNewSession(
            String refreshToken,
            String accessToken,
            PostgresUser authenticatedUser,
            DeviceInfoModel deviceInfo
    ){
        PostgresSession savedPostgresSession = this.createNewPostgresSession(
                refreshToken, authenticatedUser, deviceInfo
        );

        this.createNewRedisSession(savedPostgresSession, accessToken);
    }

    private PostgresSession createNewPostgresSession(
            String refreshToken,
            PostgresUser authenticatedUser,
            DeviceInfoModel deviceInfo
    ){
        return postgresSessionService.save(refreshToken, authenticatedUser, deviceInfo);
    }

    private void createNewRedisSession(PostgresSession savedSession, String accessToken){
        RedisSession newSession = RedisSession.builder()
                .refreshToken(savedSession.getRefreshToken())
                .isNotBlacklisted(true)
                .ownerUsername(savedSession.getOwner().getUsername())
                .accessToken(accessToken)
                .build();

        redisSessionService.save(newSession);
    }

    public RedisSession findRedisSessionByRefreshToken(String refreshToken){
        return redisSessionService.findById(refreshToken)
                .orElseThrow(() -> {
                    String errorMessage = "There's no session associated with this refreshToken";
                    return new IllegalArgumentException(errorMessage);
                });
    }

}
