package com.connect.exceptions.securityexceptions;

import org.springframework.security.core.AuthenticationException;

public class QrCodeRequestAuthenticationException extends AuthenticationException {
    public QrCodeRequestAuthenticationException(String message){
        super(message);
    }
}
