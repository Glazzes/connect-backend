package com.connect.friendship.infrastructure.controller;

import com.connect.authentication.infrastructure.security.contract.SecurityUserAdapter;
import com.connect.friendship.application.dto.FriendRequestDto;
import com.connect.friendship.application.service.FriendRequestService;
import com.connect.user.domain.entities.PostgresUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping("/request/{userId}")
    public ResponseEntity<?> saveNewFriendRequest(Authentication authentication, @PathVariable String userId){
        PostgresUser authenticatedUser = this.getAuthenticatedUser(authentication);
        friendRequestService.saveRequest(authenticatedUser, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/me/requests/number")
    public ResponseEntity<Long> findNumberOfRequestsToUser(Authentication authentication){
        return ResponseEntity.status(HttpStatus.OK)
                .body(friendRequestService.findNumberOfRequests(
                        this.getAuthenticatedUser(authentication))
                );
    }

    @GetMapping("/me/requests")
    public ResponseEntity<?> findAllAuthenticatedUserFriendRequests(Authentication authentication){
        PostgresUser authenticatedUser = this.getAuthenticatedUser(authentication);
        List<FriendRequestDto> friendRequests = friendRequestService
                .findAllAuthenticatedUserFriendRequests(authenticatedUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body(friendRequests);
    }

    @PostMapping("/request/{requestId}/accept")
    public ResponseEntity<?> acceptFriendRequest(Authentication authentication, @PathVariable String requestId){
        PostgresUser authenticatedUser = this.getAuthenticatedUser(authentication);
        friendRequestService.acceptFriendRequest(authenticatedUser, requestId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/request/{requestId}/decline")
    public ResponseEntity<?> declineFriendRequest(@PathVariable String requestId){
        friendRequestService.declineFriendRequest(requestId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    private PostgresUser getAuthenticatedUser(Authentication authentication){
        SecurityUserAdapter adapter = (SecurityUserAdapter) authentication.getPrincipal();
        return adapter.getUser();
    }

}
