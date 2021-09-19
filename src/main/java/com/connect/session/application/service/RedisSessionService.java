package com.connect.session.application.service;

import com.connect.session.domain.entities.RedisSession;
import com.connect.session.infrastructure.repository.RedisSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RedisSessionService {
    private final RedisSessionRepository redisSessionRepository;

    public RedisSession save(RedisSession session){
        return redisSessionRepository.save(session);
    }

    public Optional<RedisSession> findById(String refreshToken){
        return redisSessionRepository.findById(refreshToken);
    }

    public void deleteById(String id){
        redisSessionRepository.deleteById(id);
    }

}
