package com.connect.friendship.application.mapper;

import com.connect.friendship.application.dto.FriendRequestDto;
import com.connect.friendship.domain.entities.FriendRequest;
import com.connect.user.domain.entities.PostgresUser;

public class FriendRequestMapper {

    public static FriendRequestDto friendRequestToDto(
            FriendRequest friendRequest,
            boolean requestedByAuthenticatedUser
    ){
        PostgresUser user;
        if(requestedByAuthenticatedUser){
            user = friendRequest.getRequestedTo();
        }else{
            user = friendRequest.getRequestedBy();
        }

        return FriendRequestDto.builder()
                .friendRequestId(friendRequest.getId())
                .nickname(user.getNickname())
                .profilePicture(user.getProfilePicture())
                .connectionStatus(user.getConnectionStatus())
                .requestedByAuthenticatedUser(requestedByAuthenticatedUser)
                .build();
    }

}
