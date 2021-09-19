package com.connect.authentication.domain.model;

import com.connect.user.application.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private UserDto authenticatedUser;
}
