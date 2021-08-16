package com.connect.authentication.domain.model;

import com.connect.session.application.dto.DeviceInfoDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@RequiredArgsConstructor
public class UsernamePasswordLoginRequest {

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    @Size(min = 8)
    private String password;

    private boolean rememberMe;

    @Valid
    @NotNull
    private DeviceInfoDto deviceInfo;

}