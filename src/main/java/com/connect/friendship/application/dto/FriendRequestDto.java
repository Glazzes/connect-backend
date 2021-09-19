package com.connect.friendship.application.dto;

import com.connect.shared.enums.ConnectionStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendRequestDto {
    private String friendRequestId;
    private String nickname;
    private String profilePicture;
    private ConnectionStatus connectionStatus;
    private boolean requestedByAuthenticatedUser;
}
