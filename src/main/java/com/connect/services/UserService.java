package com.connect.services;

import com.connect.dtos.UserDto;
import com.connect.dtos.mappers.UserMapper;
import com.connect.entities.User;
import com.connect.models.SignUpRequest;
import com.connect.repositories.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Setter
@Service
public class UserService {

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public UserDto createNewUserAccount(SignUpRequest signUpRequest){
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        User newUser = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encodedPassword
        );

        User savedUser = userRepository.save(newUser);
        return UserMapper.INSTANCE.userToUserDto(savedUser);
    }

    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

}
