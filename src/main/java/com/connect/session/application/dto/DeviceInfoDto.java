package com.connect.session.application.dto;

import com.connect.shared.enums.DeviceType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DeviceInfoDto {

    @NotNull
    private DeviceType type;

    @NotNull
    @NotBlank
    private String deviceOperatingSystem;

    @NotNull
    @NotBlank
    private String deviceName;

    @NotNull
    @NotBlank
    private String deviceVersion;

}
