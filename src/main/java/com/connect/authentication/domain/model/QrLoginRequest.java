package com.connect.authentication.domain.model;

import com.connect.session.domain.model.DeviceInfoModel;
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
    private String username;

    @NotBlank
    @NotNull
    private String mobileId;

    @NotBlank
    @NotNull
    private String browserId;

    @Valid
    private DeviceInfoModel deviceInfo;
}