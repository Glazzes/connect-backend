package com.connect.shared.exception.security;

import org.springframework.security.core.AuthenticationException;

public class QrCodeRequestAuthenticationException extends AuthenticationException {
    public QrCodeRequestAuthenticationException(String message){
        super(message);
    }
}
