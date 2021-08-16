package com.connect.shared.exception;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExceptionsDetails {
    private String cause;
    private LocalDateTime thrownAt;

    public ExceptionsDetails(String cause){
        this.cause = cause;
        this.thrownAt = LocalDateTime.now();
    }

}
