package com.connect.exceptions.securityexceptions;

public class QrCodeRequestNotFoundException extends RuntimeException {
    public QrCodeRequestNotFoundException(String message){
        super(message);
    }
}