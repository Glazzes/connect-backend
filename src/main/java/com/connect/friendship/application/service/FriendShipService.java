package com.connect.friendship.application.service;

import com.connect.user.domain.entities.PostgresUser;
import com.connect.shared.exception.application.UserIdNotFoundException;
import com.connect.user.application.service.PostgresUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FriendShipService {
    private final PostgresUserService postgresUserService;

    @Transactional
    public void makeFriends(String username, String friendId){
        PostgresUser authenticatedUser = postgresUserService.findByUsername(username)
                .orElseThrow(() -> {
                    String errorMessage = String.format("""
                    No user found with username %s
                    """, username);

                    return new UserIdNotFoundException(errorMessage);
                });

        PostgresUser friendShipReceiver = postgresUserService.findById(friendId)
                .orElseThrow(() -> {
                    String errorMessage = String.format("""
                    No user found with id %s
                    """, friendId);

                    return new UserIdNotFoundException(errorMessage);
                });

        authenticatedUser.getFriends().add(friendShipReceiver);
        friendShipReceiver.getFriends().add(authenticatedUser);
    }

}
