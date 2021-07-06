package com.connect.repositories.redis;

import com.connect.entities.redis.RedisQrLoginRequest;
import org.springframework.data.repository.CrudRepository;

public interface RedisQrLoginRequestRepository extends CrudRepository<RedisQrLoginRequest, String> {
}
