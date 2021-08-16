package com.connect.session.domain.entities.embedable;

import com.connect.shared.enums.DeviceType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Embeddable
public class DeviceInfo {

    @Enumerated(value = EnumType.STRING)
    private DeviceType type;

    @Column(name = "device_operating_system", nullable = false)
    private String deviceOperatingSystem;

    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @Column(name = "device_version", nullable = false)
    private String deviceVersion;

}
