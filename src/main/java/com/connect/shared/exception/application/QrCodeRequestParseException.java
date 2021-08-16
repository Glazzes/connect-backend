package com.connect.shared.exception.application;

public class QrCodeRequestParseException extends RuntimeException{
    public QrCodeRequestParseException(String message, Throwable e){
        super(message, e);
    }
}
