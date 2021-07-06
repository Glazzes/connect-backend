package com.connect.entities.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash(value = "session")
@AllArgsConstructor
public class RedisSession {

    @Id
    private String refreshToken;

    private String associatedAuthenticationToken;

    private String owner; // username, the unique one not nickname.

    private boolean isValid;

    private boolean isNotBlacklisted;

}
