package com.connect.controllers;

import com.connect.dtos.UserDto;
import com.connect.models.SignUpRequest;
import com.connect.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDto> createNewUserAccount(
            @Valid @RequestBody SignUpRequest signUpRequest
    ){
        UserDto newUser = userService.createNewUserAccount(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newUser);
    }

}
