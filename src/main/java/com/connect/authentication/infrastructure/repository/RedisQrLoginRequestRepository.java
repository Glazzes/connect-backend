package com.connect.authentication.infrastructure.repository;

import com.connect.authentication.domain.entities.RedisQrLoginRequest;
import org.springframework.data.repository.CrudRepository;

public interface RedisQrLoginRequestRepository extends CrudRepository<RedisQrLoginRequest, String> {}
