package com.connect.shared.exception.generic;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
