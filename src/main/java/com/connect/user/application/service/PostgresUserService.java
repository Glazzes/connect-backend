package com.connect.user.application.service;

import com.connect.user.domain.entities.PostgresUser;
import com.connect.user.domain.model.SignUpRequest;
import com.connect.user.infrastructure.repository.PostgresUserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Setter
@Service
public class PostgresUserService {

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PostgresUserRepository userRepository;

    public PostgresUser createNewUserAccount(SignUpRequest signUpRequest){
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        PostgresUser newUser = new PostgresUser(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encodedPassword
        );

        return userRepository.save(newUser);
    }

    public Optional<PostgresUser> findById(String id){
        return userRepository.findById(id);
    }

    public Optional<PostgresUser> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

}
