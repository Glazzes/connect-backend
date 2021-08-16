package com.connect.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostgresUserDto {
    private String id;
    private String username;
    private String nickname;
    private String profilePicture;
}
