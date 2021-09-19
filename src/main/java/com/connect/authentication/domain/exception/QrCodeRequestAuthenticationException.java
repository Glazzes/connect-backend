package com.connect.authentication.domain.exception;

import org.springframework.security.core.AuthenticationException;

public class QrCodeRequestAuthenticationException extends AuthenticationException {
    public QrCodeRequestAuthenticationException(String message){
        super(message);
    }
}
