package com.connect.authentication.domain.model;

import com.connect.session.application.dto.DeviceInfoDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class QrLoginRequest {

    @NotNull
    @NotBlank
    private String issuedFor;

    @NotBlank
    @NotNull
    private String mobileSignature;

    @NotBlank
    @NotNull
    private String webSignature;

    @Valid
    private DeviceInfoDto deviceInfo;
}