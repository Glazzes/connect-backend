package com.connect.authentication.domain.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class QrScannedEvent {

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String nickname;

    @NotNull
    @NotBlank
    private String profilePicture;

    @NotNull
    @NotBlank
    private String mobileSignature;
}
