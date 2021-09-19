package com.connect.session.domain.model;

import com.connect.shared.enums.DeviceType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DeviceInfoModel {

    @NotNull
    private DeviceType type;

    @NotNull
    @NotBlank
    private String appDetails;

    @NotNull
    @NotBlank
    private String deviceDetails;

    @NotNull
    @NotBlank
    private String ipAddress;

}
