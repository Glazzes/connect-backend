package com.connect.authentication.domain.exception;

public class SessionUserAssociationException extends RuntimeException{
    public SessionUserAssociationException(String message){
        super(message);
    }
}
