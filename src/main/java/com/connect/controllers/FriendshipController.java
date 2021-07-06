package com.connect.controllers;

import com.connect.services.FriendShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendShipService friendShipService;

    @PostMapping("/request/{userId}")
    public ResponseEntity<?> makeFriends(Principal principal, @PathVariable String userId){
        friendShipService.makeFriends(principal.getName(), userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

}
