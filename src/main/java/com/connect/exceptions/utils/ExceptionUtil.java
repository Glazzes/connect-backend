package com.connect.exceptions.utils;

import com.connect.exceptions.details.ExceptionsDetails;

import java.time.LocalDateTime;

public class ExceptionUtil {

    public static ExceptionsDetails createDetails(
            RuntimeException exception,
            String causedBy
    ){
        return ExceptionsDetails.builder()
                .exceptionName(exception.getClass().getSimpleName())
                .errorMessage(exception.getMessage())
                .causedBy(causedBy)
                .thrownAt(LocalDateTime.now())
                .build();
    }

}
