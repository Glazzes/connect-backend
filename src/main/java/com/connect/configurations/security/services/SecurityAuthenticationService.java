package com.connect.configurations.security.services;

import com.connect.dtos.DeviceInfoDto;
import com.connect.dtos.mappers.DeviceInfoMapper;
import com.connect.entities.embedables.EmbeddableDeviceInfo;
import com.connect.services.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityAuthenticationService {
    private final SessionService sessionService;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserDetails returnUserDetailsByRefreshToken(String refreshToken){
        String username = (String) redisTemplate.opsForHash()
                .get(refreshToken, "owner");

        return userDetailsService.loadUserByUsername(username);
    }

    public void createNewSession(String refreshToken, String username, DeviceInfoDto deviceInfo){
        sessionService.saveSession(refreshToken, username, deviceInfo);
    }

    public String associateRefreshTokenWithAuthorizationToken(String refreshToken, String username){
        String authorizationToken = UUID.randomUUID().toString();

        Map<String, Object> sessionData = new HashMap<>();
        sessionData.put("owner", username);
        sessionData.put("isValid", true);
        sessionData.put("isNotBlacklisted", true);
        sessionData.put("associatedAuthenticationToken", authorizationToken);

        redisTemplate.opsForHash()
                .putAll(refreshToken, sessionData);

        return authorizationToken;
    }

}
