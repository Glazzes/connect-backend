package com.connect.session.domain.repository;

import com.connect.session.domain.entities.RedisSession;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedisSessionRepository extends CrudRepository<RedisSession, String> {
    List<RedisSession> findAllByOwner(String owner);
}
