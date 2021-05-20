package com.connect.exceptions.applicationexceptions;

public class QrCodeRequestParseException extends RuntimeException{
    public QrCodeRequestParseException(String message, Throwable e){
        super(message, e);
    }
}
