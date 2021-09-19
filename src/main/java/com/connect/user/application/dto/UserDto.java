package com.connect.user.application.dto;

import com.connect.shared.enums.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String nickname;
    private String profilePicture;
    private ConnectionStatus connectionStatus;
}
