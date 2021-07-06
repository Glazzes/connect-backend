package com.connect.repositories.redis;

import com.connect.entities.redis.RedisSession;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedisSessionRepository extends CrudRepository<RedisSession, String> {
    List<RedisSession> findAllByOwner(String owner);
}
