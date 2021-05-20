package com.connect.exceptions.applicationexceptions;

public class QrScannedEventSendException extends RuntimeException{
    public QrScannedEventSendException(String message, Throwable e){
        super(message, e);
    }
}
