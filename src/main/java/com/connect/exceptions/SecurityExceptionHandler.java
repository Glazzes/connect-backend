package com.connect.exceptions;

import com.connect.exceptions.details.ExceptionsDetails;
import com.connect.exceptions.securityexceptions.QrCodeRequestAuthenticationException;
import com.connect.exceptions.securityexceptions.QrCodeRequestNotFoundException;
import com.connect.exceptions.utils.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SecurityExceptionHandler {

    @ExceptionHandler(value = QrCodeRequestNotFoundException.class)
    public ResponseEntity<ExceptionsDetails> handleQrCodeRequestNotFoundException(
            QrCodeRequestNotFoundException exception
    ){
        String causedBy = """
        The given information attached to the qr code request has either expired or did not exist
        in the first place.
        """;

        ExceptionsDetails details = ExceptionUtil.createDetails(exception, causedBy);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(details);
    }

    @ExceptionHandler(value = QrCodeRequestAuthenticationException.class)
    public ResponseEntity<ExceptionsDetails> handleQrCodeRequestAuthenticationException(
            QrCodeRequestAuthenticationException exception
    ){
        String causedBy = """
        The current authentication did not match with the one saved on database.
        """;

        ExceptionsDetails details = ExceptionUtil.createDetails(exception, causedBy);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(details);
    }

}
