package com.connect.exceptions.details;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionsDetails {
    private String errorMessage;
    private String exceptionName;
    private String causedBy;
    private LocalDateTime thrownAt;
}
