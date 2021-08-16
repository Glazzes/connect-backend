package com.connect.user.infrastructure.controller;

import com.connect.user.domain.entities.PostgresUser;
import com.connect.user.application.dto.PostgresUserDto;
import com.connect.user.application.mapper.UserMapper;
import com.connect.user.domain.model.SignUpRequest;
import com.connect.user.application.service.ElasticUserService;
import com.connect.user.application.service.PostgresUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final PostgresUserService postgresUserService;
    private final ElasticUserService elasticUserService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<PostgresUserDto> createNewUserAccount(
            @Valid @RequestBody SignUpRequest signUpRequest
    ){
        PostgresUser newUser = postgresUserService.createNewUserAccount(signUpRequest);
        elasticUserService.saveUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserMapper.INSTANCE.userToUserDto(newUser));
    }

}
