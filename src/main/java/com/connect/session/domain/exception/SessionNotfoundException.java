package com.connect.session.domain.exception;

public class SessionNotfoundException extends RuntimeException{
    public SessionNotfoundException(String message) {
        super(message);
    }
}
