package com.connect.session.domain.entities;

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
    private String owner; // username, the unique one not the nickname.
    private boolean isValid;
    private boolean isNotBlacklisted;
}
