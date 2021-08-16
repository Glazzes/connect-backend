package com.connect.shared.exception.application;

public class QrScannedEventSendException extends RuntimeException{
    public QrScannedEventSendException(String message, Throwable e){
        super(message, e);
    }
}
