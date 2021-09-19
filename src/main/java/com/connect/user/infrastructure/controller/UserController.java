package com.connect.user.infrastructure.controller;

import com.connect.authentication.infrastructure.security.contract.SecurityUserAdapter;
import com.connect.user.application.dto.UserDto;
import com.connect.user.domain.entities.PostgresUser;
import com.connect.user.application.mapper.UserMapper;
import com.connect.user.domain.model.SignUpRequest;
import com.connect.user.application.service.ElasticUserService;
import com.connect.user.application.service.PostgresUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final PostgresUserService postgresUserService;
    // private final ElasticUserService elasticUserService;

    @PostMapping
    public ResponseEntity<UserDto> createNewUserAccount(
            @Valid @RequestBody SignUpRequest signUpRequest
    ){
        PostgresUser newUser = postgresUserService.createNewUserAccount(signUpRequest);
        // elasticUserService.save(newUser);

        log.info("A new user has been successfully created with id " + newUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserMapper.INSTANCE.userToUserDto(newUser));
    }

    @GetMapping(path = "/me")
    private ResponseEntity<UserDto> getLoggedInUser(Authentication authentication){
        SecurityUserAdapter authenticatedUser = (SecurityUserAdapter) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.OK)
                .body(UserMapper.INSTANCE.userToUserDto(authenticatedUser.getUser()));
    }

}
