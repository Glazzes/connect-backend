package com.connect.friendship.domain.exception;

public class FriendRequestNotFoundException extends RuntimeException{
    public FriendRequestNotFoundException(String message) {
        super(message);
    }
}
