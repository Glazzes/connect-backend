package com.connect.shared.exception.security;

public class QrCodeRequestNotFoundException extends RuntimeException {
    public QrCodeRequestNotFoundException(String message){
        super(message);
    }
}