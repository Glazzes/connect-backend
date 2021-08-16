package com.connect.user.application.service;

import com.connect.user.domain.entities.ElasticUser;
import com.connect.user.domain.entities.PostgresUser;
import com.connect.user.domain.repository.ElasticUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ElasticUserService {
    private final ElasticUserRepository elasticUserRepository;

    public void saveUser(PostgresUser user) {
        if(user == null || elasticUserRepository.existsById(user.getId())){
            throw new IllegalArgumentException("This id has been already used");
        }

        ElasticUser newUser = new ElasticUser(
                user.getId(),
                user.getNickname()
        );

        elasticUserRepository.save(newUser);
    }

}
