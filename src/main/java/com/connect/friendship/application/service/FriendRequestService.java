package com.connect.friendship.application.service;

import com.connect.friendship.application.dto.FriendRequestDto;
import com.connect.friendship.domain.exception.FriendRequestNotFoundException;
import com.connect.friendship.application.mapper.FriendRequestMapper;
import com.connect.friendship.domain.entities.FriendRequest;
import com.connect.friendship.domain.repository.FriendRequestRepository;
import com.connect.user.domain.entities.PostgresUser;
import com.connect.user.domain.exception.UserNotFoundException;
import com.connect.user.application.service.PostgresUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendRequestService {
    private final PostgresUserService postgresUserService;
    private final FriendRequestRepository friendRequestRepository;

    public void saveRequest(PostgresUser authenticatedUser, String friendId) {
        PostgresUser requestReceiver = this.findUserById(friendId);
        FriendRequest request = new FriendRequest(authenticatedUser, requestReceiver);
        friendRequestRepository.save(request);
    }

    private PostgresUser findUserById(String friendId){
        return postgresUserService.findById(friendId)
                .orElseThrow(() -> {
                    String errorMessage = String.format("""
                    No user found with id %s
                    """, friendId);

                    return new UserNotFoundException(errorMessage);
                });
    }

    public Long findNumberOfRequests(PostgresUser user){
        return friendRequestRepository.countAllByRequestedTo(user);
    }

    public List<FriendRequestDto> findAllAuthenticatedUserFriendRequests(PostgresUser authenticatedUser){
        List<FriendRequestDto> requestedByAuthenticatedUser = this.findAllUserRequested(authenticatedUser);
        List<FriendRequestDto> requestedToAuthenticatedUser = this.findAllRequestedToUser(authenticatedUser);

        return Stream.of(requestedByAuthenticatedUser, requestedToAuthenticatedUser)
                .flatMap(List::stream)
                .toList();
    }

    private List<FriendRequestDto> findAllUserRequested(PostgresUser authenticatedUser){
        List<FriendRequest> friendRequests = friendRequestRepository
                .findByRequestedTo(authenticatedUser);

        return friendRequests.stream()
                .map(request -> FriendRequestMapper.friendRequestToDto(request, false))
                .toList();
    }

    private List<FriendRequestDto> findAllRequestedToUser(PostgresUser authenticatedUser){
        List<FriendRequest> friendRequests = friendRequestRepository
                .findByRequestedBy(authenticatedUser);

        return friendRequests.stream()
                .map(request -> FriendRequestMapper.friendRequestToDto(request, true))
                .toList();
    }

    public void acceptFriendRequest(PostgresUser authenticatedUser, String requestId){
        PostgresUser friend = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    String errorMessage = "Could not find a friend request with id " + requestId;
                    return new FriendRequestNotFoundException(errorMessage);
                })
                .getRequestedBy();


        authenticatedUser.getFriends().add(friend);
        friend.getFriends().add(authenticatedUser);
    }

    public void declineFriendRequest(String requestId){
        if(!friendRequestRepository.existsById(requestId)){
            String errorMessage = String.format("A friend request with id %s was not found", requestId);
            throw new FriendRequestNotFoundException(errorMessage);
        }

        friendRequestRepository.deleteById(requestId);
    }

}
