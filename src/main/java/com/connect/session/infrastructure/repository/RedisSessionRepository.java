package com.connect.session.infrastructure.repository;

import com.connect.session.domain.entities.RedisSession;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RedisSessionRepository extends CrudRepository<RedisSession, String> {
    boolean existsById(String id);
}
