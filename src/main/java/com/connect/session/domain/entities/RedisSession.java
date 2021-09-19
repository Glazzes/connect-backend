package com.connect.session.domain.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@RedisHash(value = "session")
public class RedisSession {
    @Id
    private String refreshToken;
    private String accessToken;
    private String ownerUsername;
    private boolean isNotBlacklisted;
}
