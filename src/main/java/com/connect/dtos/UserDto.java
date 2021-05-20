package com.connect.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String nickname;
    private String profilePicture;
}
