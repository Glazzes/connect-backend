package com.connect.exceptions;

import com.connect.exceptions.applicationexceptions.QrCodeRequestParseException;
import com.connect.exceptions.applicationexceptions.QrScannedEventSendException;
import com.connect.exceptions.details.ExceptionsDetails;
import com.connect.exceptions.utils.ExceptionUtil;
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
        String causedBy = """
        Authentication request failed because the sent data could not be converted successfully into
        a valid request object
        """;

        ExceptionsDetails details = ExceptionUtil.createDetails(exception, causedBy);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(details);
    }

    @ExceptionHandler(value = QrScannedEventSendException.class)
    public ResponseEntity<ExceptionsDetails> handleQrScannedEventSendException(
            QrScannedEventSendException exception
    ){
        String causedBy = """
        Something went wrong while sending the qr event data to the respective browser, probably
        the SseEmitter has completed it's lifecycle
        """;

        ExceptionsDetails details = ExceptionUtil.createDetails(exception, causedBy);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(details);
    }

}
