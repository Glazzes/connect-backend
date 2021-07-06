package com.connect.services;

import com.connect.entities.User;
import com.connect.exceptions.applicationexceptions.UserIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FriendShipService {
    private final UserService userService;

    @Transactional
    public void makeFriends(String username, String friendId){
        User authenticatedUser = userService.findByUsername(username)
                .orElseThrow(() -> {
                    String errorMessage = String.format("""
                    No user found with username %s
                    """, username);

                    return new UserIdNotFoundException(errorMessage);
                });

        User friendShipReceiver = userService.findById(friendId)
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
