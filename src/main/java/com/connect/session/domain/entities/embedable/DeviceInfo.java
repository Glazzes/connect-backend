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

    @Column(name = "details", nullable = false)
    private String appDetails;

    @Column(name = "device_details")
    private String deviceDetails;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

}
