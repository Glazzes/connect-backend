package com.connect.authentication.domain.exception;

public class QrCodeRequestNotFoundException extends RuntimeException {
    public QrCodeRequestNotFoundException(String message){
        super(message);
    }
}