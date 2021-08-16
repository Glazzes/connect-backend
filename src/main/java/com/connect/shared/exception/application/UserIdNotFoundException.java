package com.connect.shared.exception.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserIdNotFoundException extends RuntimeException{
    public UserIdNotFoundException(String message){
        super(message);
    }
}
