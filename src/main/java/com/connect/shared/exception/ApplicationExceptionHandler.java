package com.connect.shared.exception;

import com.connect.shared.exception.application.QrCodeRequestParseException;
import com.connect.shared.exception.application.QrScannedEventSendException;
import com.connect.user.domain.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(value = QrCodeRequestParseException.class)
    public ResponseEntity<ExceptionsDetails> handleMalFormedQrCodeRequestException(
            QrCodeRequestParseException exception
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionsDetails(exception.getMessage()));
    }

    @ExceptionHandler(value = QrScannedEventSendException.class)
    public ResponseEntity<ExceptionsDetails> handleQrScannedEventSendException(
            QrScannedEventSendException exception
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionsDetails(exception.getMessage()));
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ExceptionsDetails> handleUserIdNotFoundException(
            UserNotFoundException exception
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionsDetails(exception.getMessage()));
    }

}
